package com.example.demo.sbm.component;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Alert properties
 * 
 * @author igor.144@gmail.com
 *
 */
@Component
@ConfigurationProperties("alert")
public class AlertProperties {

  // List of alert recepients
  private List<String> to;

  // Subject for alert when host going down
  private String hostDownSubject;

  // Subject for alert when host going up
  private String hostUpSubject;

  //Subject for alert when status going down
  private String statusDownSubject;

  // Subject for alert when status going up
  private String statusUpSubject;

  public List<String> getTo() {
    return to;
  }

  public void setTo(List<String> to) {
    this.to = to;
  }

  public String getHostDownSubject() {
    return hostDownSubject;
  }

  public void setHostDownSubject(String hostDownSubject) {
    this.hostDownSubject = hostDownSubject;
  }

  public String getHostUpSubject() {
    return hostUpSubject;
  }

  public void setHostUpSubject(String hostUpSubject) {
    this.hostUpSubject = hostUpSubject;
  }

  public String getStatusDownSubject() {
    return statusDownSubject;
  }

  public void setStatusDownSubject(String statusDownSubject) {
    this.statusDownSubject = statusDownSubject;
  }

  public String getStatusUpSubject() {
    return statusUpSubject;
  }

  public void setStatusUpSubject(String statusUpSubject) {
    this.statusUpSubject = statusUpSubject;
  }
}
