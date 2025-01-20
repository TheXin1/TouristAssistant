package org.datateam.touristassistant.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder){
        return builder.defaultSystem("You are a friendly chat bot that answers question in the voice of a Pirate").build();
    }

    @Bean
    public DocumentTransformer documentTransformer(){
        return  new TokenTextSplitter();
    }
}