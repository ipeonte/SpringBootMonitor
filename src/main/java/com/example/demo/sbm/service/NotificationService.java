package com.example.demo.sbm.service;

import com.example.demo.sbm.SbmException;
import com.example.demo.sbm.component.Alert;

/**
 * Interface for Email notificatoin service
 * 
 * @author igor.144@gmail.com
 *
 */
public interface NotificationService {

  void send(Alert alert) throws SbmException;
}
