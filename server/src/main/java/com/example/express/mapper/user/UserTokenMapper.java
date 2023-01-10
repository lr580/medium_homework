package com.example.express.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.express.entity.user.UserTokenEntity;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserTokenMapper extends BaseMapper<UserTokenEntity> {
}
