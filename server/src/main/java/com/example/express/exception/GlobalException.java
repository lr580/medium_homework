package com.example.express.exception;


public class GlobalException extends RuntimeException {
  
  public int code;
  
  public GlobalException(int code, String message) {
    super(message);
    this.code = code;
  }
  
  public GlobalException(int code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }
}