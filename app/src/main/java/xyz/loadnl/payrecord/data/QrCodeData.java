package xyz.loadnl.payrecord.data;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class QrCodeData {
    @Id(autoincrement = true)
    private Long id;
    private String memberId; //所属码商账号id
    private String deviceImei; //设备IMEI
    private String gatheringChannelCode; //收款渠道 微信 支付宝
    private String state; // 状态,启用:1;禁用:0
    private Boolean fixedGatheringAmount;
    private Double gatheringAmount; //收款金额
    private String payeeName; //收款人姓名
    private String payeePhone; //收款人手机号码
    private String payeeIdCard; //收款人身份证号码
    private String payeeCity; //收款人城市
    private String payeeAlipay; //收款人支付宝账号
    private String payeeDayLimit;//收款人支付宝日收款上限
    private String payeeMonthLimit; //收款人支付宝月收款上限
    private String codeString; //二维码识别出来的图片
    @Generated(hash = 1086125738)
    public QrCodeData(Long id, String memberId, String deviceImei,
            String gatheringChannelCode, String state, Boolean fixedGatheringAmount,
            Double gatheringAmount, String payeeName, String payeePhone,
            String payeeIdCard, String payeeCity, String payeeAlipay,
            String payeeDayLimit, String payeeMonthLimit, String codeString) {
        this.id = id;
        this.memberId = memberId;
        this.deviceImei = deviceImei;
        this.gatheringChannelCode = gatheringChannelCode;
        this.state = state;
        this.fixedGatheringAmount = fixedGatheringAmount;
        this.gatheringAmount = gatheringAmount;
        this.payeeName = payeeName;
        this.payeePhone = payeePhone;
        this.payeeIdCard = payeeIdCard;
        this.payeeCity = payeeCity;
        this.payeeAlipay = payeeAlipay;
        this.payeeDayLimit = payeeDayLimit;
        this.payeeMonthLimit = payeeMonthLimit;
        this.codeString = codeString;
    }
    @Generated(hash = 948868138)
    public QrCodeData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMemberId() {
        return this.memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    public String getDeviceImei() {
        return this.deviceImei;
    }
    public void setDeviceImei(String deviceImei) {
        this.deviceImei = deviceImei;
    }
    public String getGatheringChannelCode() {
        return this.gatheringChannelCode;
    }
    public void setGatheringChannelCode(String gatheringChannelCode) {
        this.gatheringChannelCode = gatheringChannelCode;
    }
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public Boolean getFixedGatheringAmount() {
        return this.fixedGatheringAmount;
    }
    public void setFixedGatheringAmount(Boolean fixedGatheringAmount) {
        this.fixedGatheringAmount = fixedGatheringAmount;
    }
    public Double getGatheringAmount() {
        return this.gatheringAmount;
    }
    public void setGatheringAmount(Double gatheringAmount) {
        this.gatheringAmount = gatheringAmount;
    }
    public String getPayeeName() {
        return this.payeeName;
    }
    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }
    public String getPayeePhone() {
        return this.payeePhone;
    }
    public void setPayeePhone(String payeePhone) {
        this.payeePhone = payeePhone;
    }
    public String getPayeeIdCard() {
        return this.payeeIdCard;
    }
    public void setPayeeIdCard(String payeeIdCard) {
        this.payeeIdCard = payeeIdCard;
    }
    public String getPayeeCity() {
        return this.payeeCity;
    }
    public void setPayeeCity(String payeeCity) {
        this.payeeCity = payeeCity;
    }
    public String getPayeeAlipay() {
        return this.payeeAlipay;
    }
    public void setPayeeAlipay(String payeeAlipay) {
        this.payeeAlipay = payeeAlipay;
    }
    public String getPayeeDayLimit() {
        return this.payeeDayLimit;
    }
    public void setPayeeDayLimit(String payeeDayLimit) {
        this.payeeDayLimit = payeeDayLimit;
    }
    public String getPayeeMonthLimit() {
        return this.payeeMonthLimit;
    }
    public void setPayeeMonthLimit(String payeeMonthLimit) {
        this.payeeMonthLimit = payeeMonthLimit;
    }
    public String getCodeString() {
        return this.codeString;
    }
    public void setCodeString(String codeString) {
        this.codeString = codeString;
    }
}
