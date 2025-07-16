package org.techbd.ingest.listener;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;

@Configuration
@Slf4j
public class MllpRouteRegistrar {

       private final CamelContext camelContext;

    @Value("${HL7_MLLP_PORTS}")
    private String portsRaw;

    public MllpRouteRegistrar(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @PostConstruct
    public void registerRoutes() {
        if (portsRaw == null || portsRaw.isBlank()) {
            log.warn("HL7_MLLP_PORTS is empty — no routes will be added.");
            return;
        }

        Arrays.stream(portsRaw.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .forEach(port -> {
                    try {
                        RouteBuilder route = new MllpRoute(port);
                        camelContext.addRoutes(route);
                        log.info("Registered MLLP route for port {}", port);
                    } catch (Exception e) {
                        log.error("Failed to register route for port {}", port, e);
                    }
                });
    }

}

