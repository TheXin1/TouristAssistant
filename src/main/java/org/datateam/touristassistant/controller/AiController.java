package org.datateam.touristassistant.controller;

import org.apache.ibatis.annotations.Param;
import org.datateam.touristassistant.pojo.Itinerary;
import org.datateam.touristassistant.pojo.Results;
import org.datateam.touristassistant.service.impl.AiServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public Object chatPoint(String message){
        Itinerary point = aiService.getPoint(message);
        return point;
    }



    @MessageMapping("/chat")
    public void sendMessage(@Param(value = "Authorization") String token, @Param(value = "message") String message){

    }

    //不支持m4a
    @PostMapping("/transcript")
    public ResponseEntity<?> transcript( MultipartFile file) {
        try {
            String transcript = aiService.transcript(file);
            return ResponseEntity.ok(new Results(200, true, "转录成功", transcript));
        } catch (Exception e) {
            logger.error("转录失败: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Results(500, false, "转录失败", null));
        }
    }
    //文字转语音
    @PostMapping("synthesis")
    public ResponseEntity<Resource> synthesis(String text) {
        try {
            Resource audio = aiService.synthesis(text);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=output.mp3").contentType(MediaType.parseMediaType("audio/mpeg")).body(audio);
        } catch (Exception e) {
            logger.error("语音合成失败: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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