package xyz.loadnl.payrecord.data;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

//TODO index
@Entity
public class OrderData {
    @Id(autoincrement = true)
    private Long id;
    private String orderId;
    private int status;
    private String depositor;
    private String money;
    private long create;
    private long update;
    @Generated(hash = 1394272680)
    public OrderData(Long id, String orderId, int status, String depositor,
            String money, long create, long update) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.depositor = depositor;
        this.money = money;
        this.create = create;
        this.update = update;
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
    public String getOrderId() {
        return this.orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
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
    public long getCreate() {
        return this.create;
    }
    public void setCreate(long create) {
        this.create = create;
    }
    public long getUpdate() {
        return this.update;
    }
    public void setUpdate(long update) {
        this.update = update;
    }
}
