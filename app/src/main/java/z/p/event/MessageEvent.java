package z.p.event;

public class MessageEvent {
    private boolean hasImei;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHasImei() {
        return hasImei;
    }

    public void setHasImei(boolean hasImei) {
        this.hasImei = hasImei;
    }
}
