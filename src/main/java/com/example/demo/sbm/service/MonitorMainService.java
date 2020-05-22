package com.example.demo.sbm.service;

import java.net.URISyntaxException;

/**
 * Interface for main monitor
 * 
 * @author igor.144@gmail.com
 *
 */
public interface MonitorMainService {

  void start() throws URISyntaxException;

  void stop();
}
