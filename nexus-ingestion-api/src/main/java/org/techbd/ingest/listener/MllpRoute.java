package org.techbd.ingest.listener;

import org.apache.camel.builder.RouteBuilder;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;

public class MllpRoute extends RouteBuilder {
    private final int port;

    public MllpRoute(int port) {
        this.port = port;
    }

    @Override
    public void configure() throws Exception {
        from("mllp://0.0.0.0:" + port)
            .routeId("hl7-mllp-listener-" + port)
            .log("[PORT " + port + "] Received HL7 Message:\n${body}")
            .process(exchange -> {
                String hl7Message = exchange.getIn().getBody(String.class);
                PipeParser parser = new PipeParser();

                try {
                    Message hapiMsg = parser.parse(hl7Message);
                    Message ack = hapiMsg.generateACK();
                    exchange.getMessage().setBody(parser.encode(ack));
                } catch (Exception e) {
                    String nack;
                    try {
                        Message partial = parser.parse(hl7Message);
                        nack = parser.encode(partial.generateACK("AE", new HL7Exception(e.getMessage())));
                    } catch (Exception ex2) {
                        nack = "MSH|^~\\&|||||||ACK^O01|1|P|2.3\rMSA|AE|1\r";
                    }
                    exchange.getMessage().setBody(nack);
                }
            })
            .log("[PORT " + port + "] ACK/NAK sent");
    }
}