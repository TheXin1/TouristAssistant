package org.datateam.touristassistant.pojo;

import lombok.Data;

@Data
public class ResponseMessage {
    private String status;
    private String message;
    private String token;

    public ResponseMessage(String status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }
}

