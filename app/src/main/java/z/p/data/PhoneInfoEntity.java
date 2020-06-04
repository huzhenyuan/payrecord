package z.p.data;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class PhoneInfoEntity {
    @Id
    private String k;
    private String v;

    @Generated(hash = 981912905)
    public PhoneInfoEntity(String k, String v) {
        this.k = k;
        this.v = v;
    }

    @Generated(hash = 519516684)
    public PhoneInfoEntity() {
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
