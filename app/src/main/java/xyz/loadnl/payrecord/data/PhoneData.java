package xyz.loadnl.payrecord.data;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PhoneData {
    @Id
    private String k;
    private String v;
    @Generated(hash = 101929453)
    public PhoneData(String k, String v) {
        this.k = k;
        this.v = v;
    }
    @Generated(hash = 143274380)
    public PhoneData() {
    }
    public String getK() {
        return this.k;
    }
    public void setK(String k) {
        this.k = k;
    }
    public String getV() {
        return this.v;
    }
    public void setV(String v) {
        this.v = v;
    }

}
