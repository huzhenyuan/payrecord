package z.p;

import android.app.Application;

import z.p.data.DaoMaster;
import z.p.data.DaoSession;
import z.p.util.LogcatUtil;

public class MyApplication extends Application {

    public static boolean working = false;
    private static MyApplication instances;
    private static DaoMaster.DevOpenHelper helper;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        setDatabase();
        LogcatUtil.inst.init(this);
    }

    private void setDatabase() {
        helper = new DaoMaster.DevOpenHelper(this, Const.DB_NAME, null);
        daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

}
