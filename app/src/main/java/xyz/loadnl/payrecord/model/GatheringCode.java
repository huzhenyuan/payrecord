package xyz.loadnl.payrecord.model;

import com.alibaba.fastjson.annotation.JSONField;

public class GatheringCode{

	@JSONField(name="gatheringAmount")
	private double gatheringAmount;

	@JSONField(name="payeePhone")
	private String payeePhone;

	@JSONField(name="fixedGatheringAmount")
	private boolean fixedGatheringAmount;

	@JSONField(name="payeeAlipay")
	private String payeeAlipay;

	@JSONField(name="gatheringChannelCode")
	private String gatheringChannelCode;

	@JSONField(name="payeeIdCard")
	private String payeeIdCard;

	@JSONField(name="payeeCity")
	private String payeeCity;

	@JSONField(name="payeeName")
	private String payeeName;

	@JSONField(name="deviceImei")
	private String deviceImei;

	@JSONField(name="payeeMonthLimit")
	private String payeeMonthLimit;

	@JSONField(name="createTime")
	private String createTime;

	@JSONField(name="id")
	private String id;

	@JSONField(name="state")
	private String state;

	@JSONField(name="payeeDayLimit")
	private String payeeDayLimit;

	@JSONField(name="memberId")
	private String memberId;

	@JSONField(name="storageId")
	private String storageId;

	@JSONField(name="hibernateLazyInitializer")
	private HibernateLazyInitializer hibernateLazyInitializer;

	public double getGatheringAmount(){
		return gatheringAmount;
	}

	public String getPayeePhone(){
		return payeePhone;
	}

	public boolean isFixedGatheringAmount(){
		return fixedGatheringAmount;
	}

	public String getPayeeAlipay(){
		return payeeAlipay;
	}

	public String getGatheringChannelCode(){
		return gatheringChannelCode;
	}

	public String getPayeeIdCard(){
		return payeeIdCard;
	}

	public String getPayeeCity(){
		return payeeCity;
	}

	public String getPayeeName(){
		return payeeName;
	}

	public String getDeviceImei(){
		return deviceImei;
	}

	public String getPayeeMonthLimit(){
		return payeeMonthLimit;
	}

	public String getCreateTime(){
		return createTime;
	}

	public String getId(){
		return id;
	}

	public String getState(){
		return state;
	}

	public String getPayeeDayLimit(){
		return payeeDayLimit;
	}

	public String getMemberId(){
		return memberId;
	}

	public String getStorageId(){
		return storageId;
	}

	public HibernateLazyInitializer getHibernateLazyInitializer(){
		return hibernateLazyInitializer;
	}
}