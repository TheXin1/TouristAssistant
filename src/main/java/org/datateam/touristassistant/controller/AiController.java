package org.datateam.touristassistant.controller;

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
        return aiService.generate(message);
    }

    /**
     * @param message
     * @return {@link String }
     * 普通问答
     */
    @GetMapping("/rag")
    public String chatRAG(String message){
        return aiService.generateRAG(message);
    }

    /**
     * @param message
     * @return {@link String }
     * 生成计划
     */
    @GetMapping("/plan")
    public String chatPlan(String message){
        return aiService.generatePlan(message);
    }


}