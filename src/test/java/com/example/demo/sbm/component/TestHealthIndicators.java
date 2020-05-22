package com.example.demo.sbm.component;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestHealthIndicators {

  public static boolean TEST_STATUS = true;

  @Bean
  public HealthIndicator testIndicator() {
    return new HealthIndicator() {

      @Override
      public Health health() {
        return TEST_STATUS ? Health.up().build()
            : Health.down().withDetail("ErrorCode", new Integer(1)).build();
      }
    };
  }
}
