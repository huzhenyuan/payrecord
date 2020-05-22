package xyz.loadnl.payrecord.data;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class QrCodeData {
    @Id(autoincrement = true)
    public Long id;
    public String memberId; //所属码商账号id
    public String deviceImei; //设备IMEI
    public String gatheringChannelCode; //收款渠道 微信 支付宝
    public String state; // 状态,启用:1;禁用:0
    public Boolean fixedGatheringAmount;
    public Double gatheringAmount; //收款金额
    public String payeeName; //收款人姓名
    public String payeePhone; //收款人手机号码
    public String payeeIdCard; //收款人身份证号码
    public String payeeCity; //收款人城市
    public String payeeAlipay; //收款人支付宝账号
    public String payeeDayLimit;//收款人支付宝日收款上限
    public String payeeMonthLimit; //收款人支付宝月收款上限
    public String codeString; //二维码识别出来的图片
}
