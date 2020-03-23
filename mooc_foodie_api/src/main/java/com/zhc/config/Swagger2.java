package com.zhc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

    //http://localhost:8088/swagger-ui.html  /原路径
    //http://localhost:8088/doc.html  /bootstrap路径


    //配置Swagger2核心配置 docket
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  //指定api类型为swagger2
                    .apiInfo(apiInfo())                          //响应式编程风格 用于定义api文档汇总信息
                    .select()
                    .apis(RequestHandlerSelectors.
                            basePackage("com.zhc.controller"))  //指定controller包
                    .paths(PathSelectors.any())
                    .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("天天吃货电商平台接口api")
                .contact(new Contact("zhc",
                        "https://www.baidu.com",
                        "344304180@qq.com"))
                .description("为天天吃货提供的api文档")
                .version("0.0.1")
                .termsOfServiceUrl("https://www.baidu.com")
                .build();
    }


}
