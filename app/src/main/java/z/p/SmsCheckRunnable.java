package z.p;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import z.p.data.SmsEntity;
import z.p.data.SmsEntityDao;
import z.p.util.AppUtil;
import z.p.util.LogcatUtil;


public class SmsCheckRunnable implements Runnable {

    @Override
    public void run() {
        Log.i(SmsCheckRunnable.class.getSimpleName(), "检查短信_未处理");

        List<SmsEntity> smsList = MyApplication.getDaoSession().getSmsEntityDao().queryBuilder().where(
                SmsEntityDao.Properties.ProcessFlag.eq(Const.短信_未处理)).build().list();
        for (SmsEntity smsEntity : smsList) {
            EventBus.getDefault().post(smsEntity);
        }
    }
}
