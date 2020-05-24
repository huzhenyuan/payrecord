package xyz.loadnl.payrecord.model;

import com.alibaba.fastjson.annotation.JSONField;

public class ContentItem{

	@JSONField(name="note")
	private Object note;

	@JSONField(name="payChannelName")
	private Object payChannelName;

	@JSONField(name="payTime")
	private Object payTime;

	@JSONField(name="payUrl")
	private Object payUrl;

	@JSONField(name="bankName")
	private Object bankName;

	@JSONField(name="depositTime")
	private Object depositTime;

	@JSONField(name="orderState")
	private String orderState;

	@JSONField(name="merchantName")
	private String merchantName;

	@JSONField(name="accountHolder")
	private Object accountHolder;

	@JSONField(name="gatheringCodeId")
	private String gatheringCodeId;

	@JSONField(name="depositorIp")
	private String depositorIp;

	@JSONField(name="id")
	private String id;

	@JSONField(name="payChannelId")
	private Object payChannelId;

	@JSONField(name="bankCardAccount")
	private Object bankCardAccount;

	@JSONField(name="memberId")
	private String memberId;

	@JSONField(name="usefulTime")
	private String usefulTime;

	@JSONField(name="orderNo")
	private String orderNo;

	@JSONField(name="gatheringCode")
	private GatheringCode gatheringCode;

	@JSONField(name="actualPayAmount")
	private Object actualPayAmount;

	@JSONField(name="submitTime")
	private String submitTime;

	@JSONField(name="dealTime")
	private Object dealTime;

	@JSONField(name="rechargeAmount")
	private double rechargeAmount;

	@JSONField(name="deviceImei")
	private String deviceImei;

	@JSONField(name="settlementTime")
	private Object settlementTime;

	@JSONField(name="depositor")
	private String depositor;

	@JSONField(name="orderStateName")
	private String orderStateName;

	public Object getNote(){
		return note;
	}

	public Object getPayChannelName(){
		return payChannelName;
	}

	public Object getPayTime(){
		return payTime;
	}

	public Object getPayUrl(){
		return payUrl;
	}

	public Object getBankName(){
		return bankName;
	}

	public Object getDepositTime(){
		return depositTime;
	}

	public String getOrderState(){
		return orderState;
	}

	public String getMerchantName(){
		return merchantName;
	}

	public Object getAccountHolder(){
		return accountHolder;
	}

	public String getGatheringCodeId(){
		return gatheringCodeId;
	}

	public String getDepositorIp(){
		return depositorIp;
	}

	public String getId(){
		return id;
	}

	public Object getPayChannelId(){
		return payChannelId;
	}

	public Object getBankCardAccount(){
		return bankCardAccount;
	}

	public String getMemberId(){
		return memberId;
	}

	public String getUsefulTime(){
		return usefulTime;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public GatheringCode getGatheringCode(){
		return gatheringCode;
	}

	public Object getActualPayAmount(){
		return actualPayAmount;
	}

	public String getSubmitTime(){
		return submitTime;
	}

	public Object getDealTime(){
		return dealTime;
	}

	public double getRechargeAmount(){
		return rechargeAmount;
	}

	public String getDeviceImei(){
		return deviceImei;
	}

	public Object getSettlementTime(){
		return settlementTime;
	}

	public String getDepositor(){
		return depositor;
	}

	public String getOrderStateName(){
		return orderStateName;
	}
}