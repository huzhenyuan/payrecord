package z.p.model;

import com.alibaba.fastjson.annotation.JSONField;

public class ContentItem{

	@JSONField(name="note")
	private String note;

	@JSONField(name="payChannelName")
	private String payChannelName;

	@JSONField(name="payTime")
	private String payTime;

	@JSONField(name="payUrl")
	private String payUrl;

	@JSONField(name="bankName")
	private String bankName;

	@JSONField(name="depositTime")
	private String depositTime;

	@JSONField(name="orderState")
	private String orderState;

	@JSONField(name="merchantName")
	private String merchantName;

	@JSONField(name="accountHolder")
	private String accountHolder;

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
	private String actualPayAmount;

	@JSONField(name="submitTime")
	private String submitTime;

	@JSONField(name="dealTime")
	private String dealTime;

	@JSONField(name="rechargeAmount")
	private String rechargeAmount;

	@JSONField(name="deviceImei")
	private String deviceImei;

	@JSONField(name="settlementTime")
	private String settlementTime;

	@JSONField(name="depositor")
	private String depositor;

	@JSONField(name="orderStateName")
	private String orderStateName;

	public void setNote(String note){
		this.note = note;
	}

	public String getNote(){
		return note;
	}

	public void setPayChannelName(String payChannelName){
		this.payChannelName = payChannelName;
	}

	public String getPayChannelName(){
		return payChannelName;
	}

	public void setPayTime(String payTime){
		this.payTime = payTime;
	}

	public String getPayTime(){
		return payTime;
	}

	public void setPayUrl(String payUrl){
		this.payUrl = payUrl;
	}

	public String getPayUrl(){
		return payUrl;
	}

	public void setBankName(String bankName){
		this.bankName = bankName;
	}

	public String getBankName(){
		return bankName;
	}

	public void setDepositTime(String depositTime){
		this.depositTime = depositTime;
	}

	public String getDepositTime(){
		return depositTime;
	}

	public void setOrderState(String orderState){
		this.orderState = orderState;
	}

	public String getOrderState(){
		return orderState;
	}

	public void setMerchantName(String merchantName){
		this.merchantName = merchantName;
	}

	public String getMerchantName(){
		return merchantName;
	}

	public void setAccountHolder(String accountHolder){
		this.accountHolder = accountHolder;
	}

	public String getAccountHolder(){
		return accountHolder;
	}

	public void setGatheringCodeId(String gatheringCodeId){
		this.gatheringCodeId = gatheringCodeId;
	}

	public String getGatheringCodeId(){
		return gatheringCodeId;
	}

	public void setDepositorIp(String depositorIp){
		this.depositorIp = depositorIp;
	}

	public String getDepositorIp(){
		return depositorIp;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setPayChannelId(Object payChannelId){
		this.payChannelId = payChannelId;
	}

	public Object getPayChannelId(){
		return payChannelId;
	}

	public void setBankCardAccount(Object bankCardAccount){
		this.bankCardAccount = bankCardAccount;
	}

	public Object getBankCardAccount(){
		return bankCardAccount;
	}

	public void setMemberId(String memberId){
		this.memberId = memberId;
	}

	public String getMemberId(){
		return memberId;
	}

	public void setUsefulTime(String usefulTime){
		this.usefulTime = usefulTime;
	}

	public String getUsefulTime(){
		return usefulTime;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setGatheringCode(GatheringCode gatheringCode){
		this.gatheringCode = gatheringCode;
	}

	public GatheringCode getGatheringCode(){
		return gatheringCode;
	}

	public void setActualPayAmount(String actualPayAmount){
		this.actualPayAmount = actualPayAmount;
	}

	public String getActualPayAmount(){
		return actualPayAmount;
	}

	public void setSubmitTime(String submitTime){
		this.submitTime = submitTime;
	}

	public String getSubmitTime(){
		return submitTime;
	}

	public void setDealTime(String dealTime){
		this.dealTime = dealTime;
	}

	public String getDealTime(){
		return dealTime;
	}

	public void setRechargeAmount(String rechargeAmount){
		this.rechargeAmount = rechargeAmount;
	}

	public String getRechargeAmount(){
		return rechargeAmount;
	}

	public void setDeviceImei(String deviceImei){
		this.deviceImei = deviceImei;
	}

	public String getDeviceImei(){
		return deviceImei;
	}

	public void setSettlementTime(String settlementTime){
		this.settlementTime = settlementTime;
	}

	public String getSettlementTime(){
		return settlementTime;
	}

	public void setDepositor(String depositor){
		this.depositor = depositor;
	}

	public String getDepositor(){
		return depositor;
	}

	public void setOrderStateName(String orderStateName){
		this.orderStateName = orderStateName;
	}

	public String getOrderStateName(){
		return orderStateName;
	}

	@Override
 	public String toString(){
		return 
			"ContentItem{" + 
			"note = '" + note + '\'' + 
			",payChannelName = '" + payChannelName + '\'' + 
			",payTime = '" + payTime + '\'' + 
			",payUrl = '" + payUrl + '\'' + 
			",bankName = '" + bankName + '\'' + 
			",depositTime = '" + depositTime + '\'' + 
			",orderState = '" + orderState + '\'' + 
			",merchantName = '" + merchantName + '\'' + 
			",accountHolder = '" + accountHolder + '\'' + 
			",gatheringCodeId = '" + gatheringCodeId + '\'' + 
			",depositorIp = '" + depositorIp + '\'' + 
			",id = '" + id + '\'' + 
			",payChannelId = '" + payChannelId + '\'' + 
			",bankCardAccount = '" + bankCardAccount + '\'' + 
			",memberId = '" + memberId + '\'' + 
			",usefulTime = '" + usefulTime + '\'' + 
			",orderNo = '" + orderNo + '\'' + 
			",gatheringCode = '" + gatheringCode + '\'' + 
			",actualPayAmount = '" + actualPayAmount + '\'' + 
			",submitTime = '" + submitTime + '\'' + 
			",dealTime = '" + dealTime + '\'' + 
			",rechargeAmount = '" + rechargeAmount + '\'' + 
			",deviceImei = '" + deviceImei + '\'' + 
			",settlementTime = '" + settlementTime + '\'' + 
			",depositor = '" + depositor + '\'' + 
			",orderStateName = '" + orderStateName + '\'' + 
			"}";
		}
}