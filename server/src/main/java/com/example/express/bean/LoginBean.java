package com.example.express.bean;

import com.example.express.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class LoginBean {
  private UserEntity user;
  private TokenBean auth;
}
