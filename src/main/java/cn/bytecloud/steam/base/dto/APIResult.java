package cn.bytecloud.steam.base.dto;

import java.io.Serializable;

public class APIResult implements Serializable {


    private static final long serialVersionUID = 1L;

    private final static int SUCCESS = 0;
    private final static int FAILURE = 1001;

    private int status;
    private String message = "";
    private Object value;


    public static APIResult success() {
        return new APIResult().setStatus(SUCCESS).setMessage("请求成功");
    }

    public static APIResult failure(int status) {
        if (status == 0) {
            status = 1001;
        }
        return new APIResult().setStatus(status);
    }

    public int getStatus() {
        return status;
    }

    public APIResult setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public APIResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public APIResult setValue(Object value) {
        this.value = value;
        return this;
    }

}
