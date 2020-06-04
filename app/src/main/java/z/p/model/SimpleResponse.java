package z.p.model;

import com.alibaba.fastjson.annotation.JSONField;

public class SimpleResponse {

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "code")
    private int code;

    @JSONField(name = "data")
    private String data;

    @JSONField(name = "success")
    private boolean success;

    @JSONField(name = "timestamp")
    private long timestamp;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return
                "Response{" +
                        "msg = '" + msg + '\'' +
                        ",code = '" + code + '\'' +
                        ",data = '" + data + '\'' +
                        ",success = '" + success + '\'' +
                        ",timestamp = '" + timestamp + '\'' +
                        "}";
    }
}