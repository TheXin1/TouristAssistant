package org.datateam.touristassistant.pojo;

public class Results {

    private int code;         // 自定义状态码
    private boolean ok;       // 结果标识
    private String msg;       // 结果消息
    private Object result;    // 返回的具体数据

    public Results(int code, boolean ok, String msg, Object result) {
        this.code = code;
        this.ok = ok;
        this.msg = msg;
        this.result = result;
    }

    public Results(int code, boolean ok) {
        this.code = code;
        this.ok = ok;
    }

    public Results(int code, boolean ok, String msg) {
        this.code = code;
        this.ok = ok;
        this.msg = msg;
    }

    // Getter and Setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
