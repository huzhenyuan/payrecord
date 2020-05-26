package z.p.data;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity(indexes = {@Index(value = "orderId", unique = true), @Index(value = "depositor")})
public class OrderEntity {
    @Id(autoincrement = true)
    private Long id;
    private String orderId;
    private String status;
    private String depositor;
    private String actualDepositor;
    private String rechargeAmount;
    private String actualPayAmount;
    private long create;
    private long update;
    @Generated(hash = 256637948)
    public OrderEntity(Long id, String orderId, String status, String depositor,
            String actualDepositor, String rechargeAmount, String actualPayAmount,
            long create, long update) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.depositor = depositor;
        this.actualDepositor = actualDepositor;
        this.rechargeAmount = rechargeAmount;
        this.actualPayAmount = actualPayAmount;
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
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDepositor() {
        return this.depositor;
    }
    public void setDepositor(String depositor) {
        this.depositor = depositor;
    }
    public String getActualDepositor() {
        return this.actualDepositor;
    }
    public void setActualDepositor(String actualDepositor) {
        this.actualDepositor = actualDepositor;
    }
    public String getRechargeAmount() {
        return this.rechargeAmount;
    }
    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }
    public String getActualPayAmount() {
        return this.actualPayAmount;
    }
    public void setActualPayAmount(String actualPayAmount) {
        this.actualPayAmount = actualPayAmount;
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
