package com.example.demo.sbm.component;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * Error handler for standard RestTemplate to prevent raise exception on 
 * 503 http code returned when status is down
 * 
 * @author igor.144@gmail.com
 *
 */
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

    return (httpResponse.getStatusCode().series() == Series.CLIENT_ERROR ||
        httpResponse.getStatusCode().series() == Series.SERVER_ERROR);
  }

  @Override
  public void handleError(ClientHttpResponse httpResponse) throws IOException {
    HttpStatus status = httpResponse.getStatusCode();

    if (status.series() == HttpStatus.Series.SERVER_ERROR) {
      // handle SERVER_ERROR
    } else if (status.series() == HttpStatus.Series.CLIENT_ERROR) {
      // handle CLIENT_ERROR
      if (status == HttpStatus.NOT_FOUND) {
        throw new IOException(status + " NOT FOUND");
      }
    }
  }
}