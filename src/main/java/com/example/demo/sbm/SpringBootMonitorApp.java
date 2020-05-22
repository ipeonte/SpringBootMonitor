package com.example.demo.sbm;

import java.net.URISyntaxException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.sbm.service.MonitorMainService;

/**
 * Main App launcher
 * 
 * @author igor.144@gmail.com
 * 
 */
@SpringBootApplication
public class SpringBootMonitorApp {

  @Autowired
  private MonitorMainService _srv;

  public static void main(String[] args) {
    SpringApplication.run(SpringBootMonitorApp.class);
  }

  @Bean
  public Logger logger() {
    return LoggerFactory.getLogger("Sbm");
  }

  @PostConstruct
  public void startAgents() throws URISyntaxException {
    _srv.start();
  }

  /**
   * Stop all agents
   */
  @PreDestroy
  public void stopAgents() {
    _srv.stop();
  }
}
