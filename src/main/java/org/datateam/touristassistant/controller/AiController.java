package org.datateam.touristassistant.controller;

import org.datateam.touristassistant.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    @Autowired
    AiService aiService;

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
}
