package com.example.express;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@MapperScan("com.example.express.mapper.**")
public class ExpressApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(ExpressApplication.class, args);
  }
}
