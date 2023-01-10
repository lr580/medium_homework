package com.example.express.exception;


public class LoginExpiredException extends GlobalException {
  public LoginExpiredException() {
    super(20001, "登录过期");
  }
}