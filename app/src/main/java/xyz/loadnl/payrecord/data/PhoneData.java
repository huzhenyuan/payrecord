package xyz.loadnl.payrecord.data;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class PhoneData {
    @Id(autoincrement = true)
    public Long id;
    public String imei;
    public int status; // 0 空闲， 1，正在处理收款业务
}
