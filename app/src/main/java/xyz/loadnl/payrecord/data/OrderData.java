package xyz.loadnl.payrecord.data;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class OrderData {
    @Id(autoincrement = true)
    public Long id;
    public String depositor;
    public String money;
    public long time;
    @Generated(hash = 1719309114)
    public OrderData(Long id, String depositor, String money, long time) {
        this.id = id;
        this.depositor = depositor;
        this.money = money;
        this.time = time;
    }
    @Generated(hash = 863401038)
    public OrderData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDepositor() {
        return this.depositor;
    }
    public void setDepositor(String depositor) {
        this.depositor = depositor;
    }
    public String getMoney() {
        return this.money;
    }
    public void setMoney(String money) {
        this.money = money;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
}
