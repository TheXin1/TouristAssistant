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
        return builder.defaultSystem("你是一个旅游小助手,你叫小D").build();
    }

    @Bean
    public DocumentTransformer documentTransformer(){
        return  new TokenTextSplitter();
    }

}