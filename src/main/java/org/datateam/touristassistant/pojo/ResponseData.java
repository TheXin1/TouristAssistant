package org.datateam.touristassistant.pojo;

import lombok.Data;

@Data

public class ResponseData<T> {
    private String code;
    private boolean ok;
    private String msg;
    private T result;

    public ResponseData(String code, boolean ok, String msg, T result) {
        this.code = code;
        this.ok = ok;
        this.msg = msg;
        this.result = result;
    }

}