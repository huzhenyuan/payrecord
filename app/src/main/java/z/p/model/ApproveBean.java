package z.p.model;

import com.alibaba.fastjson.annotation.JSONField;

public class ApproveBean{

	@JSONField(name="actualPayAmount")
	private String actualPayAmount;

	@JSONField(name="currentAccountId")
	private String currentAccountId;

	@JSONField(name="deviceImei")
	private String deviceImei;

	@JSONField(name="approvalResult")
	private String approvalResult;

	@JSONField(name="id")
	private String id;

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@JSONField(name="signature")
	private String signature;

	public void setActualPayAmount(String actualPayAmount){
		this.actualPayAmount = actualPayAmount;
	}

	public String getActualPayAmount(){
		return actualPayAmount;
	}

	public void setCurrentAccountId(String currentAccountId){
		this.currentAccountId = currentAccountId;
	}

	public String getCurrentAccountId(){
		return currentAccountId;
	}

	public void setDeviceImei(String deviceImei){
		this.deviceImei = deviceImei;
	}

	public String getDeviceImei(){
		return deviceImei;
	}

	public void setApprovalResult(String approvalResult){
		this.approvalResult = approvalResult;
	}

	public String getApprovalResult(){
		return approvalResult;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}
}