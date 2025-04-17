package org.datateam.touristassistant.service;

import org.datateam.touristassistant.pojo.Itinerary;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;

public interface AiService {
    String generate(String message);

    //RAG嵌入聊天
    Flux<String> generateRAG(String message);

    Flux<String> generateByPromote(String message, String promote);

    //生成旅行计划
    Flux<String> generatePlan(String message);

    Itinerary getPoint(String message);

    //语音转录api
    String transcript(MultipartFile file) throws IOException;

    //语音合成api
    Resource synthesis(String message);


}
