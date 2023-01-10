package com.example.express.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.express.bean.ResponseBean;
import com.example.express.entity.user.UserEntity;
import com.example.express.mapper.user.UserMapper;
import com.example.express.utils.TokenUtil;
import org.springframework.web.bind.annotation.*;

/**
 * ResponseBean 约定：
 *  * <p>
 *  * code: 10010
 *  * info: 非管理员
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
  
  private final UserMapper userMapper;
  
  public UserController(UserMapper userMapper) {
    this.userMapper = userMapper;
  }
  
  @GetMapping("/all")
  public ResponseBean<Page<UserEntity>> getAll(
    @RequestParam int current,
    @RequestParam int size,
    @RequestHeader(value = "Authorization") String token
  ) {
    if (isAdmin(token)) {
      Page<UserEntity> page = new Page<>(current, size);
      Page<UserEntity> result = userMapper.selectPage(page, null);
      return ResponseBean.success(result);
    } else {
      return ResponseBean.error(10010, "非管理员");
    }
  }
  
  @GetMapping("/self")
  public ResponseBean<Page<UserEntity>> getSelf(
    @RequestHeader(value = "Authorization") String token
   ) {
      UserEntity user = getUserByToken(token);
      Integer userid = user.getUserId();
      QueryWrapper<UserEntity> usQueryWrapper = 
              new QueryWrapper<>();
      usQueryWrapper.eq("user_id",userid);
      Page<UserEntity> page = new Page<>(1, 1);
      Page<UserEntity> res = userMapper.selectPage(page, usQueryWrapper);
      return ResponseBean.success(res);
  }
  
  @PostMapping("/update")
  public ResponseBean<String> update(
    @RequestParam int userId,
    @RequestParam String address,
    @RequestParam long phone,
    @RequestParam String sex,
    @RequestParam boolean isAdmin,
    @RequestHeader(value = "Authorization") String token
  ) {
    if (isAdmin(token) || isSelf(userId, token)) {
      if(!isAdmin(token) && isAdmin) {
          return ResponseBean.error(10010, "不能把自己改成管理员");
      }
      UserEntity user = userMapper.selectById(userId);
      user.setAddress(address);
      user.setPhone(phone);
      user.setSex(sex);
      user.setAdmin(isAdmin);
      userMapper.updateById(user);
      return ResponseBean.success("更新成功");
    } else {
      return ResponseBean.error(10010, "非管理员");
    }
  }
  
  @GetMapping("/find")
  public ResponseBean<UserEntity> find(
    @RequestParam int userId,
    @RequestHeader(value = "Authorization") String token
  ) {
    if (isAdmin(token) || isSelf(userId, token)) {
      return ResponseBean.success(userMapper.selectById(userId));
    } else {
      return ResponseBean.error(10010, "权限不足");
    }
  }
  
  private UserEntity getUserByToken(String token) {
      String username = TokenUtil.getUsernameByToken(token);
      QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("username", username);
      UserEntity user = userMapper.selectOne(queryWrapper);
      return user;
  }
  
  private boolean isSelf(int userId, String token) {
      UserEntity user = getUserByToken(token);
      return user.getUserId() == userId;
  }
  
  private boolean isAdmin(String token) {
    String username = TokenUtil.getUsernameByToken(token);
    QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("username", username);
    UserEntity user = userMapper.selectOne(queryWrapper);
    return user.isAdmin();
  }
}
