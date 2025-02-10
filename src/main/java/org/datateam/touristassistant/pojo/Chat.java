package org.datateam.touristassistant.pojo;

// Chat类，表示每条消息，可以是用户、系统或助手的消息
public class Chat<T extends Boolean> extends ChatBase {
    private String type; // "assistant", "user", or "system"
    private String time;
    private IsPolyline<T> isPolyline;

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public IsPolyline<T> getIsPolyline() {
        return isPolyline;
    }

    public void setIsPolyline(IsPolyline<T> isPolyline) {
        this.isPolyline = isPolyline;
    }
}
