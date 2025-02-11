package org.datateam.touristassistant.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageContent {
    private String id;
    private String content;
    private boolean hasSlice;
    private String type;
    private String time;
    private Polyline polyline;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHasSlice() {
        return hasSlice;
    }

    public void setHasSlice(boolean hasSlice) {
        this.hasSlice = hasSlice;
    }

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

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    // Polyline 内部类
    public static class Polyline {
        @JsonProperty("isPolyline")
        private boolean isPolyline;
        private String[] polyline;

        public boolean getIsPolyline() {
            return isPolyline;
        }

        public void setIsPolyline(boolean polyline) {
            isPolyline = polyline;
        }

        public String[] getPolyline() {
            return polyline;
        }

        public void setPolyline(String[] polyline) {
            this.polyline = polyline;
        }
    }
}
