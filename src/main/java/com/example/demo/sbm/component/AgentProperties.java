package com.example.demo.sbm.component;

import java.net.URL;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Agent properties
 * 
 * @author igor.144@gmail.com
 *
 */
@Component
@ConfigurationProperties("agent")
public class AgentProperties {

  private List<URL> urls;

  private int pollTime;

  private int downDelay;

  public List<URL> getUrls() {
    return urls;
  }

  public void setUrls(List<URL> urls) {
    this.urls = urls;
  }

  public int getPollTime() {
    return pollTime;
  }

  public void setPollTime(int pollTime) {
    this.pollTime = pollTime * 1000;
  }

  public int getDownDelay() {
    return downDelay;
  }

  public void setDownDelay(int downDelay) {
    this.downDelay = downDelay * 1000;
  }
}
