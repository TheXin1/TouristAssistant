package org.datateam.touristassistant.service;

public interface AiService {
    String generate(String message);

    //RAG嵌入聊天
    String generateRAG(String message);

    String generateByPromote(String message, String promote);

    //生成旅行计划
    String generatePlan(String message);
}
