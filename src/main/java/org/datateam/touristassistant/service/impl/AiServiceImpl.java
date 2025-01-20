package org.datateam.touristassistant.service.impl;

import io.micrometer.core.ipc.http.HttpSender;
import org.datateam.touristassistant.service.AiService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.DoubleStream;


@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private ChatModel chatModel;


    /**
     * @param message
     * @return {@link String }
     */
    @Override
    public String generate(String message) {
        DoubleStream OpenAiChatOptions = null;
        ChatResponse response= chatModel.call(
                new Prompt(message, OpenAiChatOptions.builder()
                        .withModel("gpt-3.5-turbo")
                        .withTemperature(0.7)
                        .build()
                )
        );

        return response.getResult().getOutput().getContent();
    }
}
