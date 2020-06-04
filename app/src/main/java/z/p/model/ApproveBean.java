package z.p.model;

import com.alibaba.fastjson.annotation.JSONField;

public class ApproveBean {

    @JSONField(name = "actualPayAmount")
    private String actualPayAmount;

    @JSONField(name = "currentAccountId")
    private String currentAccountId;

    @JSONField(name = "deviceImei")
    private String deviceImei;

    @JSONField(name = "approvalResult")
    private String approvalResult;

    @JSONField(name = "id")
    private String id;
    @JSONField(name = "signature")
    private String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getActualPayAmount() {
        return actualPayAmount;
    }

    public void setActualPayAmount(String actualPayAmount) {
        this.actualPayAmount = actualPayAmount;
    }

    public String getCurrentAccountId() {
        return currentAccountId;
    }

    public void setCurrentAccountId(String currentAccountId) {
        this.currentAccountId = currentAccountId;
    }

    public String getDeviceImei() {
        return deviceImei;
    }

    public void setDeviceImei(String deviceImei) {
        this.deviceImei = deviceImei;
    }

    public String getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(String approvalResult) {
        this.approvalResult = approvalResult;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}