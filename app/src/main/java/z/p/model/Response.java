package z.p.model;

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

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setTimestamp(long timestamp){
		this.timestamp = timestamp;
	}

	public long getTimestamp(){
		return timestamp;
	}

	@Override
 	public String toString(){
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