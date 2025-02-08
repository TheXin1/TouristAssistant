package org.datateam.touristassistant.service.impl;

import org.datateam.touristassistant.service.AiService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private VectorStore vectorStore;

    private final String PROMPT_BLUEPRINT = "请根据提供的上下文信息并适当检索，针对问题提供富有吸引力且人性化的回答。在回答时，请尽量融入一些形容词和修饰词，使内容更加生动、有趣，同时让回答自然流畅。如果上下文中没有相关信息，请提供一个合理且富有创意的回答，令读者感受到亲切和贴心。上下文：\n" +
            "{context}\n" +
            "查询问题：\n" +
            "{query}\n" +
            "回答时尽量丰富、细腻，考虑读者的需求，给出吸引人的回答。";


    private final String PROMPT_PLAN = "回答时，请务必将查询中提到的地点详细介绍，带有情感色彩的描述能让答案更加生动。在回答中请严格包括以下几点内容：\n" +
            "1. **旅行线路与景点介绍**：首先简要描述该地点的主要景点和文化背景，接着为游客规划一条流畅的旅行路线，列出可以游玩的知名景点，加入富有吸引力的描写，勾画出旅行的美好画面。\n" +
            "2. **美食推荐**：介绍该地点的特色美食，不仅列举菜肴，还要描述美食的口感和诱人的外观，尝试让读者感受到美食的香气与色彩。\n" +
            "3. **玩乐推荐**：推荐该地点的娱乐活动和独特体验，包括户外活动、夜生活、文化体验等，确保让答案具有吸引力，能够激发游客前往的兴趣。\n" +
            "回答时尽量使内容有层次感、感性丰富，同时要令读者产生亲身体验的欲望。尽量让每部分内容有具体的例子，并通过生动的细节吸引读者。";




    /**
     * @param message
     * @return {@link String }
     *
     */
    @Override
    public String generate(String message) {
        ChatResponse response= chatModel.call(
                new Prompt(message, OpenAiChatOptions.builder()
                        .withModel("gpt-3.5-turbo")
                        .withTemperature(0.7)
                        .build()
                )
        );

        return response.getResult().getOutput().getContent();
    }

    /**
     * @param message
     * @return {@link String }
     *
     *///RAG嵌入聊天
    @Override
    public String generateRAG(String message) {
        List<Document> documents =vectorStore.similaritySearch(message);

        List<String> context = documents.stream().map(Document::getContent).toList();

        PromptTemplate promptTemplate=new PromptTemplate(PROMPT_BLUEPRINT);

        Prompt p = promptTemplate.create(Map.of("context", context, "query", message));
        System.out.println(p);
        ChatResponse response= chatModel.call(p);

        return response.getResult().getOutput().getContent();
    }

    @Override
    public String generateByPromote(String message,String promote){
        List<Document> documents =vectorStore.similaritySearch(message);

        List<String> context = documents.stream().map(Document::getContent).toList();

        PromptTemplate promptTemplate=new PromptTemplate(promote);

        Prompt p = promptTemplate.create(Map.of("context", context, "query", message));
        System.out.println(p);
        ChatResponse response= chatModel.call(p);

        return response.getResult().getOutput().getContent();
    }

    //生成旅行计划
    @Override
    public String generatePlan(String message) {
        return generateByPromote(message,PROMPT_BLUEPRINT+"\n\n"+PROMPT_PLAN);
    }


    /**
     * @param text
     * @return {@link List }<{@link Float }>
     * 用于获取文本的向量化值(运用openai api)
     */
    public List<Embedding> getEmbedding(String text){
        EmbeddingResponse  embeddingResponse=embeddingModel.call(
                new EmbeddingRequest(List.of(text),
                        OpenAiEmbeddingOptions.builder().withModel("text-embedding-ada-002").build()));


        return embeddingResponse.getResults();
    }



}
