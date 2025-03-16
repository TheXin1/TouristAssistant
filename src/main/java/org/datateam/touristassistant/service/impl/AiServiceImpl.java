package org.datateam.touristassistant.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.datateam.touristassistant.pojo.Itinerary;
import org.datateam.touristassistant.service.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.*;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static groovy.json.JsonOutput.toJson;


@Slf4j
@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    @Autowired
    private OpenAiAudioSpeechModel openAiAudioSpeechModel;

    private final Logger logger = LoggerFactory.getLogger(AiServiceImpl.class);

/*    @Resource
    private AiWebsocketService aiWebsocketService;*/

    private final String PROMPT_BLUEPRINT = "请根据提供的上下文信息并适当检索，针对问题提供富有吸引力且人性化的回答。在回答时，请尽量融入一些形容词和修饰词，使内容更加生动、有趣，同时让回答自然流畅。如果上下文中没有相关信息，请提供一个合理且富有创意的回答，令读者感受到亲切和贴心。回答时，要充分考虑读者的需求，创造一种身临其境的氛围。\n" +
            "上下文：\n" +
            "{context}\n" +
            "查询问题：\n" +
            "{query}\n" +
            "回答时请关注以下几点：\n" +
            "1. 为读者提供具体而富有吸引力的信息，帮助他们更好地理解和沉浸在问题的背景中。\n" +
            "2. 根据上下文提供合适的建议和创意，务求内容既具有深度又具有人性化色彩。\n" +
            "3. 提供细腻的描写，使回答更具情感，能够引起读者的共鸣。\n" +
            "4. 适当加入形容词、比喻和修饰语，使文字更具吸引力和生动性。";



    private final String PROMPT_PLAN = "在回答时，请务必结合查询中提到的地点详细介绍，带有情感色彩的描述能让答案更加生动。回答时需要确保包括以下几点内容：\n" +
            "1. **旅行线路与景点介绍**：首先简要描述该地点的主要景点和文化背景，接着为游客规划一条流畅且详细的旅行路线，严格遵守: 景点1 -> 景点2 -> 景点3...可以带时间建议但不要列出具体时刻。每个景点之间的连接路径要详细描述，沿途的自然景色、文化背景等，尽量将每一段旅程的体验描绘得生动有趣，激发读者的探索欲望。通过景点的独特之处（如自然景观、历史文化、地方特色等）吸引读者。\n" +
            "2. **游前准备**：列出前往该地旅行的必备物品和相关注意事项，帮助游客做好充分准备，让他们在旅行中感受到你的关心与建议。注意写得既贴心又细致，能让读者想象到准备阶段的每个细节。\n" +
            "3. **美食推荐**：介绍该地点的特色美食，不仅列举菜肴，还要描述美食的口感和诱人的外观，尝试让读者感受到美食的香气与色彩。推荐美食时，尽量让描述充满感官体验，使读者有强烈的食欲。\n" +
            "4. **玩乐推荐**：推荐该地点的娱乐活动和独特体验，包括户外活动、夜生活、文化体验等，确保让答案具有吸引力，能够激发游客前往的兴趣。通过推荐的活动和体验，增强旅行的情感色彩，让游客产生亲自参与的冲动。\n" +
            "回答时尽量使内容有层次感、感性丰富，同时要令读者产生亲身体验的欲望,符合逻辑。具体例子和生动细节的加入，能够让旅行规划更加吸引人，仿佛是读者的旅行即将启程。";


    private final String GET_POINT_PROMPT =
            """
                    提取文字中的旅行线路 {context} 强严格按照以下格式返回结果：
                    1.每一天的路线必须列出，并且必须严格按照路线排列景点：
                    2.attractionName为整个景区名称不可空缺且只填景点名称;
                    3. 请确保格式严格符合要求，所有地点使用'地点名称'的形式。
                    4. 返回的必须符合JSON结构：
                    请根据这些要求提取并返回对应的路线，确保每一天的顺序和格式严格正确。注意只返回json不要多加任何语句
                    {format}
                    """;

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
     *///RAG嵌入聊天
    @Override
    public Flux<String> generateRAG(String message) {
        List<Document> documents =vectorStore.similaritySearch(message);

        List<String> context = documents.stream().map(Document::getContent).toList();

        PromptTemplate promptTemplate=new PromptTemplate(PROMPT_BLUEPRINT);

        Prompt p = promptTemplate.create(Map.of("context", context, "query", message));
        /*System.out.println(p);
        ChatResponse response= chatModel.call(p);
        return response.getResult().getOutput().getContent();*/
        return chatClient.prompt(p).stream().content();
    }

    @Override
    public Flux<String> generateByPromote(String message, String promote){
        List<Document> documents =vectorStore.similaritySearch(message);

        List<String> context = documents.stream().map(Document::getContent).toList();

        PromptTemplate promptTemplate=new PromptTemplate(promote);

        Prompt p = promptTemplate.create(Map.of("context", context, "query", message));



        /*ChatResponse response= chatModel.call(p);

        return response.getResult().getOutput().getContent();*/

        return chatClient.prompt(p).stream().content();
    }

    //生成旅行计划
    @Override
    public Flux<String> generatePlan(String message) {
        return generateByPromote(message,PROMPT_BLUEPRINT+"\n\n"+PROMPT_PLAN);
    }

    @Override
    public Itinerary getPoint(String message) {
        StructuredOutputConverter<Itinerary> itineraryStructuredOutputConverter= new BeanOutputConverter<>(Itinerary.class);
        PromptTemplate promptTemplate=new PromptTemplate(GET_POINT_PROMPT);
        Prompt p=promptTemplate.create(Map.of("context",message,"format",itineraryStructuredOutputConverter.getFormat()));
        ChatResponse response= chatModel.call(p);
        return itineraryStructuredOutputConverter.convert(response.getResult().getOutput().getContent());
    }

    @Override
    public String transcript(MultipartFile file) throws IOException {
        // 将音频文件转换为字节数组
        byte[] audioBytes = file.getBytes();

        ByteArrayResource audioResource = new ByteArrayResource(audioBytes) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        OpenAiAudioTranscriptionOptions transcriptionOptions=OpenAiAudioTranscriptionOptions.builder()
                .withModel("whisper-1")
                .withResponseFormat(
                OpenAiAudioApi.TranscriptResponseFormat.TEXT
        ).withTemperature(0f).build();

        AudioTranscriptionPrompt audioTranscriptionPrompt = new AudioTranscriptionPrompt(audioResource, transcriptionOptions);
        AudioTranscriptionResponse res = openAiAudioTranscriptionModel.call(audioTranscriptionPrompt);
        return res.getResult().getOutput();
    }

/*    public String Test() throws IOException {
        // 将音频文件转换为字节数组
        ClassPathResource resource = new ClassPathResource("测试音频.mp3");
        OpenAiAudioTranscriptionOptions transcriptionOptions=OpenAiAudioTranscriptionOptions.builder()
                .withResponseFormat(
                        OpenAiAudioApi.TranscriptResponseFormat.TEXT
                ).withTemperature(0f).build();

        AudioTranscriptionPrompt audioTranscriptionPrompt = new AudioTranscriptionPrompt(resource, transcriptionOptions);
        AudioTranscriptionResponse res = openAiAudioTranscriptionModel.call(audioTranscriptionPrompt);
        return res.getResult().getOutput();
    }*/



    @Override
    public String synthesis(String message) {

        return "";
    }



/*
    public Void answer(String message){

        Flux<String> chatResponseFlux = generatePlan(message);

        chatResponseFlux.delayElements(Duration.ofMillis(50)).subscribe(chunk ->{
            aiWebsocketService.sendMessage(chunk,"1");
        });

        return null;
    }
*/


    public List<Embedding> getEmbedding(String text){
        EmbeddingResponse  embeddingResponse=embeddingModel.call(
                new EmbeddingRequest(List.of(text),
                        OpenAiEmbeddingOptions.builder().withModel("text-embedding-ada-002").build()));
        return embeddingResponse.getResults();
    }


}
