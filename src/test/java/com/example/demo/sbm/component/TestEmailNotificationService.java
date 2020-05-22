package com.example.demo.sbm.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.sbm.SbmException;
import com.example.demo.sbm.service.NotificationService;

@Service("email_notification")
public class TestEmailNotificationService implements NotificationService {

  // Internal list to collect notifications
  private List<Alert> alerts = new ArrayList<>();

  @Override
  public void send(Alert alert) throws SbmException {
    alerts.add(alert);
  }

  public List<Alert> getAlerts() {
    return alerts;
  }

  public void clear() {
    alerts.clear();
  }
}
