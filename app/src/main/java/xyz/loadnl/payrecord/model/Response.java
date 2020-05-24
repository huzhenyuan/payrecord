package xyz.loadnl.payrecord.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Response{

	@JSONField(name="msg")
	private String msg;

	@JSONField(name="code")
	private int code;

	@JSONField(name="data")
	private Data data;

	@JSONField(name="success")
	private boolean success;

	@JSONField(name="timestamp")
	private long timestamp;

	public String getMsg(){
		return msg;
	}

	public int getCode(){
		return code;
	}

	public Data getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public long getTimestamp(){
		return timestamp;
	}
}