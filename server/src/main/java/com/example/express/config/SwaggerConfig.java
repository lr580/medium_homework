package com.example.express.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    public static final String AUTHORIZATION_HEADER = "Access-Token";
    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";
    /**
     * TODO
     * 可以根据配置读取是否开启swagger文档，针对测试与生产环境采用不同的配置
     */
    private boolean isSwaggerEnable = true;
    //是否开启swagger，正式环境一般是需要关闭的，可根据springboot的多环境配置进行设置
    //@Value(value = "${swagger.enabled}")
    Boolean swaggerEnabled = true;
    @Bean
    public Docket createRestApi() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name(AUTHORIZATION_HEADER).description("user ticket")//Token 以及Authorization 为自定义的参数，session保存的名字是哪个就可以写成那个
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数
        return new Docket(DocumentationType.SWAGGER_2)
                //.groupName("group")
                .enable(swaggerEnabled)
                .apiInfo(apiInfo()).select()
                // 对所有该包下的Api进行监控，如果想要监控所有的话可以改成any()
                //.apis(RequestHandlerSelectors.basePackage("com.iscas"))
                .apis(RequestHandlerSelectors.any())
                // 对所有路径进行扫描
                .paths(PathSelectors.any())
                .build()
                //.globalOperationParameters(pars);
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()));
    }
    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION_HEADER , AUTHORIZATION_HEADER, "header");
    }
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                //.forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
                .forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build();
    }
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference(AUTHORIZATION_HEADER, authorizationScopes));
    }
    /**
     * @return 生成文档说明信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("XXXX系统")
                .description("描述")
                //.termsOfServiceUrl("http://gaohanghang.github.io")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .version("2.0.0").build();
    }
}