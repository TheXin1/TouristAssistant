package org.datateam.touristassistant.pojo;


import java.util.List;

public class ChatResponse {

    private String id;

    private String object;
    private Long created;
    private String model;
    private String system_fingerprint;
    // GPT返回的对话列表
    private List<Choice> choices;


    public static class Choice {

        private int index;
        private Delta delta;

        private Object logprobs;
        private Object finish_reason;
    }

    static class Delta {
        private String role;
        private String content;
    }


}
