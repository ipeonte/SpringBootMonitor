package com.example.demo.sbm.component;

/**
 * Internal alert POJO
 * 
 * @author igor.144@gmail.com
 *
 */
public class Alert {

  private final String to;

  private final String subject;

  private final String body;

  private final long dts;

  public Alert(String to, String subject, String body) {
    this.dts = System.currentTimeMillis();
    this.to = to;
    this.subject = subject;
    this.body = body;
  }

  public String getTo() {
    return to;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }

  public long getDateStamp() {
    return dts;
  }

}