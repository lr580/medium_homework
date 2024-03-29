package com.example.express.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Data
@AllArgsConstructor
@TableName(value = "user")
public class UserEntity {
  @TableId
  private Integer userId;
  @NotNull
  private String username;
  @Nullable
  private String address;
  @Nullable
  private Long phone;
  @Nullable
  private String sex;
  private boolean isAdmin;
}
