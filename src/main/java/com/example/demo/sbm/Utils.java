package com.example.demo.sbm;

/**
 * Generic utilities
 * 
 * @author igor.144@gmail.com
 * 
 */
public class Utils {

  /**
   * Convert first character to upper case in input word
   * 
   * @param msg input word
   * @return word with first character upper case
   */
  public static String ucFirstChar(String msg) {
    char first = Character.toUpperCase(msg.charAt(0));
    return first + msg.substring(1);
  }
}
