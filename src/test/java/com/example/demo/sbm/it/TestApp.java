package com.example.demo.sbm.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.sbm.TestConfiguration;
import com.example.demo.sbm.component.AgentProperties;
import com.example.demo.sbm.component.Alert;
import com.example.demo.sbm.component.AlertProperties;
import com.example.demo.sbm.component.TestEmailNotificationService;
import com.example.demo.sbm.component.TestHealthIndicators;
import com.example.demo.sbm.service.MonitorMainService;
import com.example.demo.sbm.service.impl.AlertServiceImpl;
import com.example.demo.sbm.service.impl.MonitorMainServiceImpl;

/**
 * Integration test
 * 
 * @author igor.144@gmail.com
 *
 */
@SpringBootTest(classes = { TestConfiguration.class, AgentProperties.class,
    AlertProperties.class, AlertServiceImpl.class, MonitorMainServiceImpl.class,
    TestHealthIndicators.class, TestEmailNotificationService.class },
    webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:/test.properties")
public class TestApp {

  @Autowired
  private AgentProperties _props;

  //@Autowired
  //private AlertAction _alerts;

  @Autowired
  private TestEmailNotificationService _nsrv;

  @LocalServerPort
  int port;

  @Autowired
  private MonitorMainService _srv;

  @Test
  public void testBaseUpDown() throws Exception {
    // Modify test agent properties
    _props.setUrls(Arrays.asList(new URL[] {
        new URL("http://localhost:" + port + "/actuator/health") }));

    // Check alert queue empty
    assertEquals("Alert queues is not empty.", 0, _nsrv.getAlerts().size());

    // Start monitor
    _srv.start();

    // Wait 1 sec to retrieve original health status
    Thread.sleep(1000);

    // Check alert queue empty
    assertEquals("Alert queues is not empty.", 0, _nsrv.getAlerts().size());

    checkAlert(false, "down");

    checkAlert(true, "back to normal");

    _srv.stop();
  }

  private void checkAlert(boolean status, String msg)
      throws InterruptedException {
    // Change status of health indicator
    TestHealthIndicators.TEST_STATUS = status;

    // Wait 1.5 sec to update indicator
    Thread.sleep(1500);

    // Check alert
    assertEquals("Alert queues is empty.", 1, _nsrv.getAlerts().size());
    Alert alert = _nsrv.getAlerts().get(0);
    assertNotNull("Alert is null.", alert);
    assertEquals("Subject doesn't match",
        "Host localhost:" + port + " status is " + msg, alert.getSubject());

    Pattern p = Pattern
        .compile("Date: \\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}");

    if (!p.matcher(alert.getBody()).matches())
      fail("Fail matching body " + alert.getBody());

    // Delete alert after check
    _nsrv.getAlerts().remove(0);
  }
}
