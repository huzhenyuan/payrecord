package z.p;

import android.app.Application;

import z.p.data.DaoMaster;
import z.p.data.DaoSession;
import z.p.util.LogcatUtil;

public class MyApplication extends Application {

    public static boolean working = false;
    private static DaoSession daoSession;

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setDatabase();
        LogcatUtil.inst.init(this);
    }

    private void setDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Const.DB_NAME, null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

}
