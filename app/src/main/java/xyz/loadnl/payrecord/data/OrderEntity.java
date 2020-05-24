package xyz.loadnl.payrecord.data;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity(indexes = {@Index(value = "orderId", unique = true), @Index(value = "depositor")})
public class OrderEntity {
    @Id(autoincrement = true)
    private Long id;
    private String orderId;
    private int status;
    private String depositor;
    private String money;
    private long create;
    private long update;
    @Generated(hash = 1713134754)
    public OrderEntity(Long id, String orderId, int status, String depositor, String money,
            long create, long update) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.depositor = depositor;
        this.money = money;
        this.create = create;
        this.update = update;
    }
    @Generated(hash = 959685193)
    public OrderEntity() {
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
