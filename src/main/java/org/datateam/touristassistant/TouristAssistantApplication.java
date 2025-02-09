package org.datateam.touristassistant;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("org.datateam.touristassistant.mapper")
/*@ServletComponentScan  //测试阶段关闭拦截器*/
public class TouristAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(TouristAssistantApplication.class, args);
    }

}
