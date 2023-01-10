package com.example.express.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@TableName(value = "user_secret")
public class UserSecretEntity {
  @TableId
  private String username;
  private String password;
}
