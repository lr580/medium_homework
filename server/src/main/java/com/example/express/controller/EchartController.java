package com.example.express.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.express.bean.ResponseBean;
import com.example.express.entity.article.ArticleEntity;
import com.example.express.mapper.article.ArticleMapper;

@RestController
@RequestMapping(value = "/stat")
public class EchartController {
    @Autowired
    private ArticleMapper articleMapper;

    @GetMapping(value = "/a")
    public ResponseBean<String> test() {
        return ResponseBean.success("你好");
    }

    @GetMapping(value = "/getGoodsPricesRange")
    public ResponseBean<List<Integer>> getGoodsPricesRange() {
        List<Integer> lt = new LinkedList<>();
        for (int i = 0; i < 4; ++i) {
            lt.add(0);
        }
        List<ArticleEntity> goods = articleMapper.selectList(null);
        int n = goods.size();
        for (int i = 0; i < n; ++i) {
            ArticleEntity good = goods.get(i);
            double price = good.getCost();
            if (price <= 10.0) {
                lt.set(0, lt.get(0) + 1);
            } else if (price <= 100.0) {
                lt.set(1, lt.get(1) + 1);
            } else if (price <= 1000.0) {
                lt.set(2, lt.get(2) + 1);
            } else {
                lt.set(3, lt.get(3) + 1);
            }
        }
        return ResponseBean.success(lt);
    }
}
