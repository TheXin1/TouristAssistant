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

    private final String PROMPT_BLUEPRINT = """
      Answer the query strictly referring the provided context:
      {context}
      Query:
      {query}
      In case you don't have any answer from the context provided, just say:
      I'm sorry I don't have the information you are looking for.
    """;



    /**
     * @param message
     * @return {@link String }
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

    //RAG嵌入聊天
    @Override
    public String generateRAG(String message) {
        List<Document> documents =vectorStore.similaritySearch(message);

        List<String> context = documents.stream().map(Document::getContent).toList();

        PromptTemplate promptTemplate=new PromptTemplate(PROMPT_BLUEPRINT);

        Prompt p = promptTemplate.create(Map.of("context", context, "query", message));

        ChatResponse response= chatModel.call(p);

        return response.getResult().getOutput().getContent();
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
