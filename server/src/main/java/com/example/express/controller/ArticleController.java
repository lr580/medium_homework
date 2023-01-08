package com.example.express.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.express.bean.ResponseBean;
import com.example.express.entity.article.ArticleEntity;
import com.example.express.entity.express.ExpressEntity;
import com.example.express.entity.user.UserEntity;
import com.example.express.mapper.article.ArticleMapper;
import com.example.express.mapper.user.UserMapper;
import com.example.express.utils.CheckAdminUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;
    public ArticleController(ArticleMapper articleMapper, UserMapper userMapper) {
        this.articleMapper = articleMapper;
        this.userMapper = userMapper;
    }

    @GetMapping("/all")
    public ResponseBean<Page<ArticleEntity>> getAll(
            @RequestParam int current,
            @RequestParam int size
    ) {
        // 这里正常情况需要检查 token，是否是管理员，但检查操作交给了拦截器
        Page<ArticleEntity> page = new Page<>(current,size);
        Page<ArticleEntity> result = articleMapper.selectPage(page,null);
        return ResponseBean.success(result);
    }

    @GetMapping("/find")
    public ResponseBean<Page<ArticleEntity>> find(
        @RequestParam String key,
        @RequestParam int current,
        @RequestParam int size
    ) {
        Page<ArticleEntity> page = new Page<>(current, size);
        QueryWrapper<ArticleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("article_id", key)
          .or().like("article", key)
          .or().like("article_inf", key);
        return ResponseBean.success(articleMapper.selectPage(page, queryWrapper));
    }
    //删除指定的数据
    @PostMapping("/delete")
    public ResponseBean<String> delete(
            @RequestParam int articleId,
            @RequestHeader(value = "Authorization") String token
    ) {
        UserEntity user = CheckAdminUtil.check(token, userMapper);//为检查是否管理员做准备
        if (user.isAdmin()) {
            QueryWrapper<ArticleEntity> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("article_id", articleId);
            int delete = articleMapper.delete(queryWrapper2);
            return ResponseBean.success("成功删除" + delete + "条数据");
        }
        return ResponseBean.error(10010, "权限不足，删除失败");
    }


    //更新商品信息
    @PostMapping("/update")
    public ResponseBean<String> update(
            @RequestBody ArticleEntity articleEntity,
            @RequestHeader(value = "Authorization") String token
    ) {
        UserEntity user = CheckAdminUtil.check(token, userMapper);//为检查是否管理员做准备
        if (user.isAdmin()) {
            QueryWrapper<ArticleEntity> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("article_id", articleEntity.getArticleId());
            int update = articleMapper.update(articleEntity, queryWrapper2);//逗号前四传入的实体
            if (update == 0) {
                return ResponseBean.error(10020, "该数据不存在");
            }
            return ResponseBean.success("修改成功");
        }
        return ResponseBean.error(10010, "权限不足，修改失败");
    }

    //添加商品信息
    @PostMapping("/add")
    public ResponseBean<String> add(
            @RequestBody ArticleEntity articleEntity,
            @RequestHeader(value = "Authorization") String token
    ) {
        UserEntity user = CheckAdminUtil.check(token, userMapper);//为检查是否管理员做准备
        if (user.isAdmin()) {
            ArticleEntity articleEntity1 = articleMapper.selectById(articleEntity.getArticleId());
            if (articleEntity1 != null) {
                return ResponseBean.error(10030, "该条记录已存在");
            }
            articleMapper.insert(articleEntity);//商品实体
            return ResponseBean.success("增加信息成功");
        }
        return ResponseBean.error(10010, "权限不足，增加信息失败");
    }
}
