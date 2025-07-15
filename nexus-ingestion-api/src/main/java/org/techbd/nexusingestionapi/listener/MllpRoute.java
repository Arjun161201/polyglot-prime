package org.techbd.nexusingestionapi.listener;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.camel.Processor;

import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
@Component
public class MllpRoute extends RouteBuilder {
    
    @Value("${hl7.mllp.ports}")   
    private String[] ports;
   
    @Override
    public void configure() throws Exception {
        if (ports == null || ports.length == 0) {
        log.warn("No ports configured in HL7_MLLP_PORT. No MLLP listener started.");
        return;
    }
    for (String portStr : ports) {
        try {
            int port = Integer.parseInt(portStr.trim());
            createMllpRoute(port);
        } catch (NumberFormatException ex) {
            log.error("Invalid port number in HL7_MLLP_PORT: " + portStr, ex);
        }
    }
    }
     private void createMllpRoute(int port) {
        from("mllp://0.0.0.0:" + port)
            .routeId("hl7-mllp-listener-" + port)
            .log("[PORT " + port + "] Received HL7 Message:\n${body}")
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    String hl7Message = exchange.getIn().getBody(String.class);
                    PipeParser parser = new PipeParser();

                    try {
                        // Parse incoming HL7 message
                        Message hapiMsg = parser.parse(hl7Message);

                        // Generate standard ACK (MSA|AA)
                        Message ack = hapiMsg.generateACK();
                        String ackString = parser.encode(ack);

                        // Set ACK response
                        exchange.getMessage().setBody(ackString);
                        exchange.setProperty("ackType", "AA");

                    } catch (Exception e) {
                        log.error("[PORT " + port + "] Error while parsing or generating ACK", e);

                        String nackString;
                        try {
                            Message partialMsg = parser.parse(hl7Message);
                            @SuppressWarnings("deprecation")
                            Message nack = partialMsg.generateACK("AE", new HL7Exception(e.getMessage()));
                            nackString = parser.encode(nack);
                        } catch (Exception innerEx) {
                            nackString = "MSH|^~\\&|||||||ACK^O01|1|P|2.3\rMSA|AE|1\r";
                        }

                        exchange.getMessage().setBody(nackString);
                        exchange.setProperty("ackType", "AE");
                    }
                }
            })
            .log("[PORT " + port + "] HL7 ACK/NAK message sent.");
     }
}