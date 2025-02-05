package org.datateam.touristassistant;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan
public class TouristAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(TouristAssistantApplication.class, args);
    }

}
