package z.p.data;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity(indexes = {@Index(value = "smsId", unique = true)})
public class SmsEntity {
    @Id(autoincrement = true)
    private Long id;
    private String smsId;
    private String phoneNumber;
    private String smsContent;
    @Generated(hash = 1458557575)
    public SmsEntity(Long id, String smsId, String phoneNumber, String smsContent) {
        this.id = id;
        this.smsId = smsId;
        this.phoneNumber = phoneNumber;
        this.smsContent = smsContent;
    }
    @Generated(hash = 1127714058)
    public SmsEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSmsId() {
        return this.smsId;
    }
    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getSmsContent() {
        return this.smsContent;
    }
    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }
}