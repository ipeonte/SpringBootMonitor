package com.example.demo.sbm.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Email notification properties
 * 
 * @author igor.144@gmail.com
 *
 */
@Component
@ConfigurationProperties("email")
public class EmailProperties {

  private String username;

  private String password;

  private String mailFrom;

  private String mailServer;

  private int mailPort;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getMailFrom() {
    return mailFrom;
  }

  public void setMailFrom(String mailFrom) {
    this.mailFrom = mailFrom;
  }

  public String getMailServer() {
    return mailServer;
  }

  public void setMailServer(String mailServer) {
    this.mailServer = mailServer;
  }

  public int getMailPort() {
    return mailPort;
  }

  public void setMailPort(int mailPort) {
    this.mailPort = mailPort;
  }
}
