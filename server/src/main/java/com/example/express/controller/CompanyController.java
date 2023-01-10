package com.example.express.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.express.bean.ResponseBean;
import com.example.express.entity.company.CompanyEntity;
import com.example.express.entity.express.ExpressEntity;
import com.example.express.entity.user.UserEntity;
import com.example.express.mapper.company.CompanyMapper;
import com.example.express.mapper.user.UserMapper;
import com.example.express.utils.CheckAdminUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {
  
  private final CompanyMapper companyMapper;
  private final UserMapper userMapper;
  public CompanyController(CompanyMapper companyMapper, UserMapper userMapper) {
    this.companyMapper = companyMapper;
    this.userMapper = userMapper;
  }
  
  @GetMapping("/all")
  public ResponseBean<Page<CompanyEntity>> getALl(
    @RequestParam int current,
    @RequestParam int size
  ) {
    Page<CompanyEntity> page = new Page<>(current, size);
    Page<CompanyEntity> result = companyMapper.selectPage(page, null);
    return ResponseBean.success(result);
  }
  
  @GetMapping("find")
  public ResponseBean<Page<CompanyEntity>> find(
    @RequestParam String key,
    @RequestParam int current,
    @RequestParam int size
  ) {
    Page<CompanyEntity> page = new Page<>(current, size);
    QueryWrapper<CompanyEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.like("company_id", key)
      .or().like("company", key)
      .or().like("address", key)
      .or().like("company_inf", key);
    return ResponseBean.success(companyMapper.selectPage(page, queryWrapper));
  }
  //删除指定的数据
  @PostMapping("/delete")
  public ResponseBean<String> delete(
          @RequestParam int companyId,
          @RequestHeader(value = "Authorization") String token
  ) {
    UserEntity user = CheckAdminUtil.check(token, userMapper);//为检查是否管理员做准备
    if (user.isAdmin()) {
      QueryWrapper<CompanyEntity> queryWrapper2 = new QueryWrapper<>();
      queryWrapper2.eq("company_id", companyId);
      int delete = companyMapper.delete(queryWrapper2);
      return ResponseBean.success("成功删除" + delete + "条数据");
    }
    return ResponseBean.error(10010, "权限不足，删除失败");
  }


  //更新快递信息
  @PostMapping("/update")
  public ResponseBean<String> update(
          @RequestBody CompanyEntity companyEntity,
          @RequestHeader(value = "Authorization") String token
  ) {
    UserEntity user = CheckAdminUtil.check(token, userMapper);//为检查是否管理员做准备
    if (user.isAdmin()) {
      QueryWrapper<CompanyEntity> queryWrapper2 = new QueryWrapper<>();
      queryWrapper2.eq("company_id", companyEntity.getCompanyId());
      int update = companyMapper.update(companyEntity, queryWrapper2);//逗号前四传入的实体
      if (update == 0) {
        return ResponseBean.error(10020, "该数据不存在");
      }
      return ResponseBean.success("修改成功");
    }
    return ResponseBean.error(10010, "权限不足，修改失败");
  }

  //添加公司信息
  @PostMapping("/add")
  public ResponseBean<String> add(
          @RequestBody CompanyEntity companyEntity,
          @RequestHeader(value = "Authorization") String token
  ) {
    UserEntity user = CheckAdminUtil.check(token, userMapper);//为检查是否管理员做准备
    if (user.isAdmin()) {
      CompanyEntity companyEntity1 = companyMapper.selectById(companyEntity.getCompanyId());
      if (companyEntity1 != null) {
        return ResponseBean.error(10030, "该条记录已存在");
      }
      companyMapper.insert(companyEntity);//公司实体
      return ResponseBean.success("增加信息成功");
    }
    return ResponseBean.error(10010, "权限不足，增加信息失败");
  }
}
