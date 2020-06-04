package z.p;

import android.app.Application;

import z.p.util.LogcatUtil;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogcatUtil.inst.init(this);
    }
}
