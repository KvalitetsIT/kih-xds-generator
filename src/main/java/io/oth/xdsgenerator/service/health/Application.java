package io.oth.xdsgenerator.service.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class Application implements HealthIndicator {
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private final String message_key = "started";

    private final LocalDateTime startTime = LocalDateTime.now();

    @Override
    public Health health() {
        return Health.up().withDetail(message_key, startTime).build();
    }

}
