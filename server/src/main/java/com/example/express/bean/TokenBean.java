package com.example.express.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenBean {
  private String token;
  private String refreshToken;
}
