package org.datateam.touristassistant.controller;

import org.datateam.touristassistant.service.AiService;
import org.datateam.touristassistant.service.impl.AiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    @Autowired
    private AiServiceImpl aiService;

    /**
     * @param message
     * @return {@link String }
     * @description 测试聊天功能
     */
    @RequestMapping("/chatTest")
    public String chatTest(String message){
        message="请问你是谁?";
        return aiService.generate(message);
    }

    @GetMapping("/rag")
    public String chatRAG(String message){
        return aiService.generateRAG(message);
    }


}
