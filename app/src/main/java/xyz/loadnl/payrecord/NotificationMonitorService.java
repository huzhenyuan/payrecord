package xyz.loadnl.payrecord;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.loadnl.payrecord.data.DaoMaster;
import xyz.loadnl.payrecord.data.OrderData;
import xyz.loadnl.payrecord.data.OrderDataDao;
import xyz.loadnl.payrecord.util.AppUtil;
import xyz.loadnl.payrecord.util.CryptoUtil;

import static xyz.loadnl.payrecord.AppConst.SERVER;

public class NotificationMonitorService extends NotificationListenerService {


    public long lastTimePosted = System.currentTimeMillis();
    private Pattern pAlipay;

    private MediaPlayer mediaPlayer;
    private PowerManager.WakeLock wakeLock;
    private DaoMaster.DevOpenHelper helper;
    private DaoMaster daoMaster;


    public void onCreate() {
        super.onCreate();
        int version = AppUtil.getVersionCode(this);

        Toast.makeText(getApplicationContext(), "启动服务", Toast.LENGTH_LONG).show();
        //支付宝
        String pattern = "(\\S*)通过扫码向你付款([\\d\\.]+)元";
        pAlipay = Pattern.compile(pattern);

        mediaPlayer = MediaPlayer.create(this, R.raw.payrecv);
        MediaPlayer payNetWorkError = MediaPlayer.create(this, R.raw.networkerror);

        Log.i(AppConst.TAG, "Notification Monitor Service start");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(AppConst.CHANNEL_ID);
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(AppConst.CHANNEL_ID, "payRecord", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("个人支付的监控");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this, AppConst.CHANNEL_ID);//

        nb.setContentTitle("PayRecord").setTicker("PayRecord个人支付").setSmallIcon(R.drawable.ic_monetization_on_black_24dp);
        nb.setContentText("个人支付运行中.请保持此通知一直存在");
        nb.setWhen(System.currentTimeMillis());
        Notification notification = nb.build();
        startForeground(1, notification);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //保持cpu一直运行，不管屏幕是否黑屏
        if (pm != null && wakeLock == null) {
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
            wakeLock.acquire(7 * 24 * 60 * 60 * 1000L /*10 minutes*/);
        }

        helper = new DaoMaster.DevOpenHelper(this, AppConst.DB_NAME, null);
        daoMaster = new DaoMaster(helper.getWritableDatabase());

        AlarmManager alarmManager=(AlarmManager)getSystemService(Service.ALARM_SERVICE);
    }


    public void onDestroy() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
        Intent localIntent = new Intent();
        localIntent.setClass(this, NotificationMonitorService.class);
        startService(localIntent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Bundle bundle = sbn.getNotification().extras;
        String pkgName = sbn.getPackageName();
        if (getPackageName().equals(pkgName)) {
            Log.i(AppConst.TAG, "测试成功");
            Intent intent = new Intent();
            intent.setAction(AppConst.IntentAction);
            Uri uri = new Uri.Builder().scheme("app").path("log").query("msg=测试成功").build();
            intent.setData(uri);
            sendBroadcast(intent);
            playMedia(mediaPlayer);
            return;
        }
        String title = bundle.getString("android.title");
        String text = bundle.getString("android.text");
        Log.d(AppConst.TAG, "Notification posted [" + pkgName + "]:" + title + " & " + text);
        if (TextUtils.isEmpty(text)) {
            //没有消息.
            return;
        }
        this.lastTimePosted = System.currentTimeMillis();
        if (pkgName.equals("com.eg.android.AlipayGphone")) {
            do {
                Matcher m = pAlipay.matcher(text);
                if (m.find()) {
                    String uname = m.group(1);
                    String money = m.group(2);
                    postMethod(money, uname);
                    break;
                }
                Log.w(AppConst.TAG, "匹配失败" + text);
            } while (false);
        }
    }


//    @Override
//    public void run() {
//         while (true) {
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                Log.e(TAG, "service thread", e);
//            }
//            if(!sendingList.isEmpty()) {
//                OrderData data;
//                synchronized (sendingList){
//                    data = sendingList.remove(0);
//                }
//                postMethod(data);
//            }
//            long now = System.currentTimeMillis();
//            do {
//                //10秒内有交互,取消
//                if (now - lastNetTime < 10000) {
//                    Log.d(TAG, "10秒内有交互");
//                    break;
//                }
//                //发送在线通知,保持让系统时时刻刻直到app在线,5秒发送一次
//                if (now - lastSendTime < 5000) {
//                    Log.d(TAG, "5秒内有交互");
//                    break;
//                }
//                postState();
//                //20秒,没消息了.提示网络异常
//                if (now - lastNetTime > 20000) {
//                    playMedia(payNetWorkError);
//                }
//            } while (false);
//        }
//    }


    public void onNotificationRemoved(StatusBarNotification paramStatusBarNotification) {
        if (Build.VERSION.SDK_INT >= 19) {
            Bundle localObject = paramStatusBarNotification.getNotification().extras;
            String pkgName = paramStatusBarNotification.getPackageName();
            String title = localObject.getString("android.title");
            String text = (localObject).getString("android.text");
            Log.e(AppConst.TAG, "Notification removed [" + pkgName + "]:" + title + " & " + text);
        }
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        return START_NOT_STICKY;
    }

    public void postMethod(final String money, final String depositor) {
        //收到支付成功的系统通知后，找到本地记录的未完成的充值订单，把订单的状态修改了
        List<OrderData> list = daoMaster.newSession().getOrderDataDao().queryBuilder()
                .where(OrderDataDao.Properties.Status.eq(0))
                .where(OrderDataDao.Properties.Depositor.eq(depositor))
                .where(OrderDataDao.Properties.Money.eq(money)).build().list();
        if (list.isEmpty()) {
            return;
        }
        String orderId = list.get(0).getOrderId();

        playMedia(mediaPlayer);
        OrderData orderData = new OrderData();
        orderData.setDepositor(depositor);
        orderData.setMoney(money);
        orderData.setUpdate(System.currentTimeMillis());
        daoMaster.newSession().getOrderDataDao().insert(orderData);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");


        JSONObject approvalJson = new JSONObject();
        try {
            approvalJson.put("currentAccountId", AppConst.MEMBER_ID);
            approvalJson.put("id", orderId);
            approvalJson.put("actualPayAmount", money);
            approvalJson.put("approvalResult", "2");
            approvalJson.put("deviceImei", AppUtil.getImei());

            String content = approvalJson.toString();
            String signature = CryptoUtil.sign(AppConst.PRIVATE_KEY, content);

            approvalJson.put("signature", signature);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(mediaType, approvalJson.toString());
        Request request = new Request.Builder()
                .url(SERVER + "recharge/approval")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseString = response.body().string();
            Log.i(AppConst.TAG, "提交收款信息结果：" + responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playMedia(MediaPlayer media) {
        if (AppConst.PlaySounds) {
            media.start();
        }
    }
}