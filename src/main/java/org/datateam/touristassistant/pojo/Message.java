package org.datateam.touristassistant.pojo;

import lombok.Data;
import org.bouncycastle.util.test.FixedSecureRandom;

import java.time.LocalDateTime;


public class Message {
    private int id;
    private String openid;
    private String content;
    private String type;
    private LocalDateTime time;

    public Message() {
    }

    public Message(String openid, String content, String type, LocalDateTime time) {
        this.openid = openid;
        this.content = content;
        this.type = type;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
