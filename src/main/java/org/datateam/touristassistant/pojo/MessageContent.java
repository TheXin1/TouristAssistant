package org.datateam.touristassistant.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // 忽略 JSON 中未知字段
public class MessageContent {
    private long id;
    private String content;
    private boolean hasSlice;
    private String type;
    private String time;
    private Polyline polyline;
    private List<List<Double>> position;

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        private List<List<Double>> polyline;

        public Polyline() {
        }

        public Polyline(boolean isPolyline, List<List<Double>> polyline) {
            this.isPolyline = isPolyline;
            this.polyline = polyline;
        }

        public boolean getIsPolyline() {
            return isPolyline;
        }

        public void setIsPolyline(boolean polyline) {
            isPolyline = polyline;
        }

        public List<List<Double>> getPolyline() {
            return polyline;
        }

        public void setPolyline(List<List<Double>> polyline) {
            this.polyline = polyline;
        }
    }
}
