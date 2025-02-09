package org.datateam.touristassistant.controller;

import org.datateam.touristassistant.service.impl.AiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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


}