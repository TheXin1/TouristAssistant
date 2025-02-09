package org.datateam.touristassistant.service;

import reactor.core.publisher.Flux;

public interface AiService {
    String generate(String message);

    //RAG嵌入聊天
    Flux<String> generateRAG(String message);

    Flux<String> generateByPromote(String message, String promote);

    //生成旅行计划
    Flux<String> generatePlan(String message);
}
