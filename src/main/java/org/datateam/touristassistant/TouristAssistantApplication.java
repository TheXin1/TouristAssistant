package org.datateam.touristassistant;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("org.datateam.touristassistant.mapper")
/*@ServletComponentScan  //测试阶段关闭拦截器*/
public class TouristAssistantApplication {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public AutowireCapableBeanFactory autowireCapableBeanFactory() {
        return applicationContext.getAutowireCapableBeanFactory();
    }
    public static void main(String[] args) {
        SpringApplication.run(TouristAssistantApplication.class, args);
    }

}
