package io.oth.xdsgenerator.service.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class XdsHealthCheckIndicator implements HealthIndicator {
    private static final Logger log = LoggerFactory.getLogger(XdsHealthCheckIndicator.class);
    private final String message_key = "XDS Repository";

    @Value("${xds.iti41.endpoint}")
    String xdsIti41Endpoint;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Health health() {

        XdsHealthCheckReply reply = isXdsReady();

        if (reply.isReady()) {
            return Health.up().withDetail(message_key, reply.getMessage()).build();
        } else {
            return Health.down().withDetail(message_key, reply.getMessage()).build();
        }

    }

    private XdsHealthCheckReply isXdsReady() {
        log.debug("Starting xds health check");
        XdsHealthCheckReply reply = new XdsHealthCheckReply();

        try {
            ResponseEntity<String> result = restTemplate.exchange(xdsIti41Endpoint+"?wsdl", HttpMethod.GET, null, String.class);
            log.warn("Got reply " + result.getStatusCode().getReasonPhrase());

            int statusCode = result.getStatusCode().value();

            if (statusCode < 400) {
                reply.setReady(true);
                reply.setMessage("Online and ready");
            }
        } catch (RestClientException e) {
            reply.setMessage(e.getLocalizedMessage());
        }


        log.debug("End xds health check");
        return reply;
    }

class XdsHealthCheckReply {
    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    private boolean isReady;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

}

}
