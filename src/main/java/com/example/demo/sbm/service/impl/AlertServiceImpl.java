package com.example.demo.sbm.service.impl;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.sbm.Constants;
import com.example.demo.sbm.SbmException;
import com.example.demo.sbm.component.Alert;
import com.example.demo.sbm.component.AlertProperties;
import com.example.demo.sbm.service.AlertAction;
import com.example.demo.sbm.service.NotificationService;

/**
 * Alert Service implementation
 * 
 * @author igor.144@gmail.com
 *
 */
@Service
public class AlertServiceImpl implements AlertAction {

  @Autowired
  private AlertProperties _props;

  @Autowired
  private Logger _log;

  // Email notification service
  @Autowired
  @Qualifier("email_notification")
  private NotificationService _email;

  // Email notification queue
  private final BlockingQueue<Alert> _mqueue = new LinkedBlockingQueue<>();

  // Notification services
  Map<String, Thread> _notifiers = new HashMap<>();

  @PostConstruct
  private void initNotificationServices() {
    // Start mail notification service
    Thread t =
        new Thread(new NotificationUpdater("email", _email, _mqueue, _log));
    t.start();
    _notifiers.put("email", t);
  }

  @Override
  public synchronized void alert(URI host, Boolean isHostDown,
      Map<String, Object> failedServices) {
    // Retrieve host:port from URI
    String hname;

    try {
      hname = host.toURL().getHost() + ":" + host.toURL().getPort();
    } catch (MalformedURLException e) {
      _log.error(
          "Error retrieve host:port from [" + host + "] - " + e.getMessage());
      return;
    }

    // Quickly queue alert separately for each recipient
    for (String rcp : _props.getTo()) {
      // Check if host down or up
      if (isHostDown != null) {
        if (isHostDown)
          _mqueue.add(new Alert(rcp,
              String.format(_props.getHostDownSubject(), hname), ""));
        else
          _mqueue.add(new Alert(rcp,
              String.format(_props.getHostUpSubject(), hname), ""));
      } else if (failedServices != null) {
        Object status = failedServices.get(Constants.STATUS_NAME);
        if (status == null) {
          // Send custom alert
          _mqueue.add(new Alert(rcp, Constants.STATUS_UNKNOWN,
              failedServices.get(Constants.STATUS_UNKNOWN).toString()));
        } else {
          switch (status.toString()) {
          case Constants.STATUS_UP:
            _mqueue.add(new Alert(rcp,
                String.format(_props.getStatusUpSubject(), hname),
                "Date: " + LocalDateTime.now()));
            break;
          case Constants.STATUS_DOWN:
            _mqueue.add(new Alert(rcp,
                String.format(_props.getStatusDownSubject(), hname),
                "Date: " + LocalDateTime.now()));
            break;
          default:
            _mqueue.add(new Alert(rcp, Constants.STATUS_UNKNOWN,
                "Unknown status [" + status + "]"));
          }
        }
      } else {
        _log.warn("Unknown alert with NULL host down flag and NULL status");
      }
    }
  }
}

class NotificationUpdater implements Runnable {

  // Notificatin service
  private NotificationService _service;

  // Alert queue for given service
  private BlockingQueue<Alert> _queue;

  // Logger
  private Logger _log;

  // Notification Updater name
  private String _name;

  public NotificationUpdater(String name, NotificationService service,
      BlockingQueue<Alert> queue, Logger log) {
    _log = log;
    _queue = queue;
    _service = service;
  }

  @Override
  public void run() {
    while (true) {
      try {
        Alert alert = _queue.take();

        // Repeat until succeed
        boolean sent = false;

        while (!sent) {
          try {
            _service.send(alert);
            sent = true;
          } catch (SbmException e) {
            _log.error("Error sending alert to " + _name + " updater");
            // Wait and re-try
            Thread.sleep(1000);
          }
        }
      } catch (InterruptedException e) {
        // Do nothing, just interrupt thread
        break;
      }
    }
  }
}