package xyz.loadnl.payrecord.util;

import android.content.Context;
import android.content.pm.PackageManager;


public class AppUtil {
    private static String imei;

    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getImei() {
        return imei;
    }

    public static void setImei(String imei) {
        AppUtil.imei = imei;
    }
}
