package com.example.demo.sbm;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class TestConfiguration {

  @Bean
  public Logger logger() {
    return TestConstants.TEST_LOG;
  }
}
