/*
 * Open Source Business Intelligence Tools - http://www.osbitools.com/
 * 
 * Copyright 2014-2018 IvaLab Inc. and by respective contributors (see below).
 * 
 * Released under the LGPL v3 or higher
 * See http://www.gnu.org/licenses/lgpl.txt
 *
 * Contributors:
 * 
 * Igor Peonte <igor.144@gmail.com>
 *
 * Date: @date
 * 
 */
package com.example.demo.sbm.service.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.sbm.SbmException;
import com.example.demo.sbm.component.Alert;
import com.example.demo.sbm.component.EmailProperties;
import com.example.demo.sbm.service.NotificationService;

/**
 * Email notification service
 * 
 * @author igor.144@gmail.com
 *
 */
@Service("email_notification")
public class EmailNotificationServiceImpl implements NotificationService {

  @Autowired
  private EmailProperties _props;

  @Autowired
  private Logger _log;

  @Override
  public void send(Alert alert) throws SbmException {

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", _props.getMailServer());
    props.put("mail.smtp.port", _props.getMailPort());
    props.put("mail.smtp.ssl.trust", _props.getMailServer());

    Session session =
        Session.getInstance(props, new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(_props.getUsername(),
                _props.getPassword());
          }
        });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(_props.getMailFrom()));
      message.setRecipients(Message.RecipientType.TO,
          InternetAddress.parse(alert.getTo()));
      message.setSubject(alert.getSubject());
      message.setText(alert.getBody());

      Transport.send(message);
    } catch (MessagingException e) {
      _log.error("Error sending mail to host " + _props.getMailServer() + ":" +
          _props.getMailPort() + " - " + e.getMessage());
      throw new SbmException("Email notification error");
    }
  }

}
