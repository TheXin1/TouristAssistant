package org.datateam.touristassistant.service;

import org.springframework.stereotype.Service;


public interface AiService {

    //获取生成回答
    public String generate(String message);

    //获取基于RAG回答
    public String generateRAG(String message);

    public String generateByPromote(String message, String promote);

    public String generatePlan(String message);
}
