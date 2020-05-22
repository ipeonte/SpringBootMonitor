package com.example.demo.sbm.service.impl;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import com.example.demo.sbm.agent.MonitorAgent;
import com.example.demo.sbm.component.AgentProperties;
import com.example.demo.sbm.service.MonitorMainService;

/**
 * Main monitor service
 * 
 * @author igor.144@gmail.com
 *
 */
@Service
public class MonitorMainServiceImpl implements MonitorMainService {

  @Autowired
  private AgentProperties props;

  @Autowired
  private AlertServiceImpl emailAlert;

  @Autowired
  private Logger log;

  @Autowired
  private RestTemplateBuilder _builder;

  // List of all monitoring thread
  private final List<Thread> mthreads = new ArrayList<>();

  @Override
  public void start() throws URISyntaxException {
    // Start all monitoring threads
    for (URL url : props.getUrls()) {
      Thread t =
          new Thread(new MonitorAgent(_builder, url, props, emailAlert, log));
      mthreads.add(t);
      t.start();
    }
  }

  @Override
  public void stop() {
    for (Thread t : mthreads)
      t.interrupt();
  }

}
