package com.example.demo.sbm.agent;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.sbm.Constants;
import com.example.demo.sbm.component.AgentProperties;
import com.example.demo.sbm.component.RestTemplateResponseErrorHandler;
import com.example.demo.sbm.service.AlertAction;

/**
 * Single agent monitor
 * 
 * @author igor.144@gmail.com
 * 
 * 
 */
public class MonitorAgent implements Runnable {

  private final URI _uri;

  private final AgentProperties _props;

  private final RestTemplate _rest;

  private final Logger _log;

  // Host Up/Down status
  private final ItemStatus _host;

  // Host service status
  private final ItemStatus _service;

  // Alert callback
  private final AlertAction _action;

  public MonitorAgent(RestTemplateBuilder restTemplateBuilder, URL url,
      AgentProperties props, AlertAction action, Logger log)
      throws URISyntaxException {
    _uri = url.toURI();
    _props = props;
    _action = action;
    _log = log;

    _host = new HostStatus(_props.getDownDelay(), _log);
    _service = new ServiceStatus(_props.getDownDelay(), _log);

    _rest = restTemplateBuilder
        .errorHandler(new RestTemplateResponseErrorHandler()).build();
  }

  @Override
  public void run() {
    // Start network polling
    while (true) {
      // Run cycle
      _run();

      try {
        Thread.sleep(_props.getPollTime());
      } catch (InterruptedException e) {
        _log.error("Terminating polling thread for host [" + _uri + "]");
        break;
      }
    }
  }

  /**
   * Run polling cycle
   */
  private void _run() {
    try {
      @SuppressWarnings("unchecked")
      Map<String, String> status = _rest.getForObject(_uri, Map.class);

      // Check main status
      if (status == null || status.size() == 0) {
        // Report error but continue
        _log.error("Invalid response received from host " + _uri);
      }

      // Host is up.
      if (_host.setUp()) {
        // Set UP alert
        _log.info("host " + _uri + " UP");
        _action.alert(_uri, false, null);
        return;
      }

      // Checking status
      Map<String, Object> services = new HashMap<>();

      String sname = status.get(Constants.STATUS_NAME);
      if (sname == null) {
        _log.error("Invalid response from host " + _uri);

        // Invalid response. Same as status down
        services.put(Constants.STATUS_UNKNOWN,
            "Missing status field in http response");

        _action.alert(_uri, null, services);
        return;
      }

      // Analyze status flag
      switch (sname) {
      case Constants.STATUS_UP:
        if (_service.setUp()) {
          _log.info("Host " + _uri + " sending status up alert.");
          services.put("status", "UP");
          _action.alert(_uri, null, services);
        }
        break;

      case Constants.STATUS_DOWN:
        if (_service.setDown()) {
          _log.error("Host " + _uri + " sending status down alert.");
          services.put("status", "DOWN");
          _action.alert(_uri, null, services);
        }
        break;

      default:
        // Set alert for invalid status
        _log.error(
            "Invalid response from host " + _uri + " status [" + sname + "]");

        // Invalid response. Same as status down
        services.put(Constants.STATUS_UNKNOWN,
            "Invalid status response [" + sname + "]");
      }

    } catch (RestClientException e) {
      _log.error("Failed connecting to " + _uri);

      // Set host down
      if (_host.setDown())
        _action.alert(_uri, true, null);
    }
  }
}

abstract class ItemStatus {
  // Item Down flag
  private boolean _down;

  // Time when down flag was set
  private long _dime;

  // Delay to report alert
  private int _delay;

  // Flag that down alert was sent
  private boolean _dalert;

  // Logger
  private Logger _log;

  abstract String getItemType();

  // Delay to 
  public ItemStatus(int downDelay, Logger log) {
    _delay = downDelay;
    _log = log;
  }

  public boolean isDown() {
    return _down;
  }

  public void setDown(boolean down) {
    this._down = down;
  }

  public Boolean setDown() {
    // Set item down
    if (!_down) {
      _down = true;
      _dime = System.currentTimeMillis();
      _log.error("Detected " + getItemType() + " status down.");
    } else {
      // Check how long is down
      if (System.currentTimeMillis() - _dime > _delay) {
        // Send alert down
        if (!_dalert) {
          _dalert = true;
          return true;
        }
      }
    }

    return false;
  }

  public boolean setUp() {
    // Set item up
    if (_down) {
      _down = false;
      _dalert = false;
      _log.error("Detected " + getItemType() + " status up.");

      // Send alert right away
      return true;
    }

    return false;
  }
}

class HostStatus extends ItemStatus {

  public HostStatus(int downDelay, Logger log) {
    super(downDelay, log);
  }

  @Override
  String getItemType() {
    return "host";
  }
}

class ServiceStatus extends ItemStatus {

  public ServiceStatus(int downDelay, Logger log) {
    super(downDelay, log);
  }

  @Override
  String getItemType() {
    return "service";
  }
}