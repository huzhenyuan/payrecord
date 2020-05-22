package xyz.loadnl.payrecord;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class AlarmService extends Service {
    /**
     * 每1分钟更新一次数据
     */
    private static final int INTERVAL = 5 * 1000;
    private static final int PENDING_REQUEST = 0;

    AlarmManager alarmManager;
    PendingIntent pIntent;

    public AlarmService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //通过AlarmManager定时启动广播
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + INTERVAL;//从开机到现在的毫秒书（手机睡眠(sleep)的时间也包括在内
        Intent i = new Intent(this, AlarmReceiver.class);
        pIntent = PendingIntent.getBroadcast(this, PENDING_REQUEST, i, PENDING_REQUEST);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
