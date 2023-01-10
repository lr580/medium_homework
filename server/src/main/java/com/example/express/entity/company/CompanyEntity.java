package com.example.express.entity.company;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "company")
public class CompanyEntity {
  @TableId
  private Integer companyId;
  private String company;
  private String address;
  private String companyInf;
}
