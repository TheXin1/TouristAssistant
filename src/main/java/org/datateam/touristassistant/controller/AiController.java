package org.datateam.touristassistant.controller;

import org.apache.ibatis.annotations.Param;
import org.datateam.touristassistant.pojo.Results;
import org.datateam.touristassistant.service.impl.AiServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    @Autowired
    private AiServiceImpl aiService;

    private final Logger logger = LoggerFactory.getLogger(AiServiceImpl.class);



    @RequestMapping("/chatTest")
    public String chatTest(String message){
        return aiService.generate(message);
    }


    @GetMapping("/rag")
    public Flux<String> chatRAG(String message){
        return aiService.generateRAG(message);
    }


    @GetMapping("/plan")
    public Flux<String> chatPlan(String message){
        return aiService.generatePlan(message);
    }

    @GetMapping("/point")
    public String chatPoint(String message){
        return aiService.getPoint(message).toString();
    }


    @MessageMapping("/chat")
    public void sendMessage(@Param(value = "Authorization") String token, @Param(value = "message") String message){

    }

    //不支持m4p
    @PostMapping("/transcript")
    public ResponseEntity<?> transcript(@RequestBody MultipartFile file) {
        try {
            String transcript = aiService.transcript(file);
            return ResponseEntity.ok(new Results(200, true, "转录成功", transcript));
        } catch (Exception e) {
            logger.error("转录失败: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Results(500, false, "转录失败", null));
        }
    }


  /*  @GetMapping("/test")
    public String test(){
        try {
            return aiService.Test();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

}