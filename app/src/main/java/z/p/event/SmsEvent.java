package z.p.event;

public class SmsEvent {

    private String smsId;
    private String phoneNumber;

    private String smsContent;

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }


    @Override
    public String toString() {
        return "SmsEvent{" +
                "smsId='" + smsId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", smsContent='" + smsContent + '\'' +
                '}';
    }
}
