package io.oth.xdsgenerator.service.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class XdsHealthCheckIndicator implements HealthIndicator {
    private static final Logger log = LoggerFactory.getLogger(XdsHealthCheckIndicator.class);
    private final String message_key = "XDS Repository";
    private final String ping_failed_message_key = "Last failed ping";
    private final String ping_succeeded_message_key = "Last successful ping";

    private LocalDateTime lastSuccessfulPing;
    private LocalDateTime lastFailedPing;

    @Value("${xds.iti41.endpoint}")
    String xdsIti41Endpoint;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Health health() {

        XdsHealthCheckReply reply = isXdsReady();

        if (reply.isReady()) {
            return Health.up()
                    .withDetail(message_key, reply.getMessage())
                    .withDetail(ping_succeeded_message_key, reply.getLastSuccessfulPing() != null ? reply.getLastSuccessfulPing() : "never")
                    .withDetail(ping_failed_message_key, reply.getLastFailedPing() != null ? reply.getLastFailedPing() : "never")
                    .build();
        } else {
            return Health.down()
                    .withDetail(message_key, reply.getMessage())
                    .withDetail(ping_succeeded_message_key, reply.getLastSuccessfulPing() != null ? reply.getLastSuccessfulPing() : "never")
                    .withDetail(ping_failed_message_key, reply.getLastFailedPing() != null ? reply.getLastFailedPing() : "never")
                    .build();
        }

    }

    private XdsHealthCheckReply isXdsReady() {
        log.debug("Starting xds health check");
        XdsHealthCheckReply reply = new XdsHealthCheckReply(this.lastSuccessfulPing, this.lastFailedPing);

        try {
            ResponseEntity<String> result = restTemplate.exchange(xdsIti41Endpoint + "?wsdl", HttpMethod.GET, null, String.class);
            log.warn("Got reply " + result.getStatusCode().getReasonPhrase());

            int statusCode = result.getStatusCode().value();

            if (statusCode < 400) {
                this.lastSuccessfulPing = LocalDateTime.now();
                reply.setLastSuccessfulPing(this.lastSuccessfulPing);
                reply.setReady(true);
                reply.setMessage("Online and ready");
            } else {
                this.lastFailedPing = LocalDateTime.now();
                reply.setLastFailedPing(this.lastFailedPing);
            }

        } catch (RestClientException e) {
            reply.setMessage(e.getLocalizedMessage());
            lastFailedPing = LocalDateTime.now();
            reply.setLastFailedPing(this.lastFailedPing);
        }


        log.debug("End xds health check");

        reply.setLastSuccessfulPing(this.lastSuccessfulPing);
        reply.setLastFailedPing(this.lastFailedPing);
        return reply;
    }

    class XdsHealthCheckReply {
        public XdsHealthCheckReply(LocalDateTime lastSuccessfulPing, LocalDateTime lastFailedPing) {
            this.lastSuccessfulPing = lastSuccessfulPing;
            this.lastFailedPing = lastFailedPing;
        }

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

        private LocalDateTime lastFailedPing;

        public LocalDateTime getLastFailedPing() {
            return lastFailedPing;
        }

        public void setLastFailedPing(LocalDateTime lastFailedPing) {
            this.lastFailedPing = lastFailedPing;
        }

        private LocalDateTime lastSuccessfulPing;

        public LocalDateTime getLastSuccessfulPing() {
            return lastSuccessfulPing;
        }

        public void setLastSuccessfulPing(LocalDateTime lastSuccessfulPing) {
            this.lastSuccessfulPing = lastSuccessfulPing;
        }

    }

}
