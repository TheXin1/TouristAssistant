package org.datateam.touristassistant.controller;

import org.apache.ibatis.annotations.Param;
import org.datateam.touristassistant.service.impl.AiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    @Autowired
    private AiServiceImpl aiService;

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



}