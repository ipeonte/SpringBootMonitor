package com.example.demo.sbm.it;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.sbm.component.EmailProperties;

@RunWith(SpringRunner.class)
@EnableConfigurationProperties
@SpringBootTest(classes = { EmailProperties.class })
@TestPropertySource("classpath:/test.properties")
public class EmailPropertiesTest {

  @Autowired
  private EmailProperties _props;

  @Test
  public void testEmailProperties() {
    assertEquals("Mail Server doesn't match.", "localhost",
        _props.getMailServer());
    assertEquals("Mail Port doesn't match.", 587, _props.getMailPort());
    assertEquals("Mail From doesn't match.", "test@localhost",
        _props.getMailFrom());
    assertEquals("Username doesn't match.", "test", _props.getUsername());
    assertEquals("Password doesn't match.", "****", _props.getPassword());
  }
}
