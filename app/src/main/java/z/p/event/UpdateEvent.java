package z.p.event;

public class UpdateEvent {
    private int versionCode;

    public UpdateEvent(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
