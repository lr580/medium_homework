package com.example.express.exception;


public class TokenException extends GlobalException {
  public TokenException() {
    super(20000, "token 异常");
  }
}
