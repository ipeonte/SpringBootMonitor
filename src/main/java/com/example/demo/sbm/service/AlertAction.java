package com.example.demo.sbm.service;

import java.net.URI;
import java.util.Map;

/**
 * Interface for host & status up/down alerting service
 * 
 * @author igor.144@gmail.com
 *
 */
public interface AlertAction {

  void alert(URI host, Boolean isDown, Map<String, Object> failedServices);
}
