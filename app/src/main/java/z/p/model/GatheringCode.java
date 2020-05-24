package z.p.model;

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

	public void setGatheringAmount(double gatheringAmount){
		this.gatheringAmount = gatheringAmount;
	}

	public double getGatheringAmount(){
		return gatheringAmount;
	}

	public void setPayeePhone(String payeePhone){
		this.payeePhone = payeePhone;
	}

	public String getPayeePhone(){
		return payeePhone;
	}

	public void setFixedGatheringAmount(boolean fixedGatheringAmount){
		this.fixedGatheringAmount = fixedGatheringAmount;
	}

	public boolean isFixedGatheringAmount(){
		return fixedGatheringAmount;
	}

	public void setPayeeAlipay(String payeeAlipay){
		this.payeeAlipay = payeeAlipay;
	}

	public String getPayeeAlipay(){
		return payeeAlipay;
	}

	public void setGatheringChannelCode(String gatheringChannelCode){
		this.gatheringChannelCode = gatheringChannelCode;
	}

	public String getGatheringChannelCode(){
		return gatheringChannelCode;
	}

	public void setPayeeIdCard(String payeeIdCard){
		this.payeeIdCard = payeeIdCard;
	}

	public String getPayeeIdCard(){
		return payeeIdCard;
	}

	public void setPayeeCity(String payeeCity){
		this.payeeCity = payeeCity;
	}

	public String getPayeeCity(){
		return payeeCity;
	}

	public void setPayeeName(String payeeName){
		this.payeeName = payeeName;
	}

	public String getPayeeName(){
		return payeeName;
	}

	public void setDeviceImei(String deviceImei){
		this.deviceImei = deviceImei;
	}

	public String getDeviceImei(){
		return deviceImei;
	}

	public void setPayeeMonthLimit(String payeeMonthLimit){
		this.payeeMonthLimit = payeeMonthLimit;
	}

	public String getPayeeMonthLimit(){
		return payeeMonthLimit;
	}

	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	public String getCreateTime(){
		return createTime;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setPayeeDayLimit(String payeeDayLimit){
		this.payeeDayLimit = payeeDayLimit;
	}

	public String getPayeeDayLimit(){
		return payeeDayLimit;
	}

	public void setMemberId(String memberId){
		this.memberId = memberId;
	}

	public String getMemberId(){
		return memberId;
	}

	public void setStorageId(String storageId){
		this.storageId = storageId;
	}

	public String getStorageId(){
		return storageId;
	}

	public void setHibernateLazyInitializer(HibernateLazyInitializer hibernateLazyInitializer){
		this.hibernateLazyInitializer = hibernateLazyInitializer;
	}

	public HibernateLazyInitializer getHibernateLazyInitializer(){
		return hibernateLazyInitializer;
	}

	@Override
 	public String toString(){
		return 
			"GatheringCode{" + 
			"gatheringAmount = '" + gatheringAmount + '\'' + 
			",payeePhone = '" + payeePhone + '\'' + 
			",fixedGatheringAmount = '" + fixedGatheringAmount + '\'' + 
			",payeeAlipay = '" + payeeAlipay + '\'' + 
			",gatheringChannelCode = '" + gatheringChannelCode + '\'' + 
			",payeeIdCard = '" + payeeIdCard + '\'' + 
			",payeeCity = '" + payeeCity + '\'' + 
			",payeeName = '" + payeeName + '\'' + 
			",deviceImei = '" + deviceImei + '\'' + 
			",payeeMonthLimit = '" + payeeMonthLimit + '\'' + 
			",createTime = '" + createTime + '\'' + 
			",id = '" + id + '\'' + 
			",state = '" + state + '\'' + 
			",payeeDayLimit = '" + payeeDayLimit + '\'' + 
			",memberId = '" + memberId + '\'' + 
			",storageId = '" + storageId + '\'' + 
			",hibernateLazyInitializer = '" + hibernateLazyInitializer + '\'' + 
			"}";
		}
}