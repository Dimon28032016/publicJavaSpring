package org.example.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
@ComponentScan(basePackages = "org.example.web") //<context:component-scan base-package="org.example.web"/>
@EnableWebMvc  //<mvc:annotation-driven/>
public class WebContextConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registration) {
        //<mvc:resources mapping="/**" location="classpath:images"/>
        registration.addResourceHandler("/**").addResourceLocations("classpath:/images");
        registration.addResourceHandler("/**").addResourceLocations("classpath:/static");
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        //class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/views/"); //<property name="prefix" value="/WEB-INF/views/"/>
        templateResolver.setSuffix(".html");  //    <property name="suffix" value=".html"/>
        templateResolver.setTemplateMode("HTML5"); //    <property name="templateMode" value="HTML"/>
        templateResolver.setCacheable(true); //    <property name="cacheable" value="true"/>

        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        ////class="org.thymeleaf.spring5.SpringTemplateEngine">
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());  //    <property name="templateResolver" ref="templateResolver"/>

        return  templateEngine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        // <bean class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine()); //    <property name="templateEngine" ref="templateEngine"/>
        thymeleafViewResolver.setOrder(1); //    <property name="order" value="1"/>

        return thymeleafViewResolver;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(10 * 1024 * 1024); //5MB
        return commonsMultipartResolver;
    }

}
