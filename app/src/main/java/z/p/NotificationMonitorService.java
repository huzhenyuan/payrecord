package z.p;

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

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import z.p.data.DaoMaster;
import z.p.data.OrderEntity;
import z.p.data.OrderEntityDao;
import z.p.model.ApproveBean;
import z.p.util.AppUtil;
import z.p.util.CryptoUtil;

import static z.p.Const.SERVER;
import static z.p.Const.充值订单状态_已支付;
import static z.p.Const.充值订单状态_待支付;

public class NotificationMonitorService extends NotificationListenerService {


    public long lastTimePosted = System.currentTimeMillis();
    private Pattern pAlipay;

    private MediaPlayer mediaPlayer;
    private PowerManager.WakeLock wakeLock;
    private DaoMaster.DevOpenHelper helper;
    private DaoMaster daoMaster;


    public void onCreate() {
        super.onCreate();
        String version = AppUtil.getVersionName(this);

        Toast.makeText(getApplicationContext(), "启动服务", Toast.LENGTH_LONG).show();
        //支付宝
        String pattern = "(\\S*)通过扫码向你付款([\\d\\.]+)元";
        pAlipay = Pattern.compile(pattern);

        mediaPlayer = MediaPlayer.create(this, R.raw.payrecv);
        MediaPlayer payNetWorkError = MediaPlayer.create(this, R.raw.networkerror);

        Log.i(Const.TAG, "Notification Monitor Service start");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(Const.CHANNEL_ID);
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(Const.CHANNEL_ID, "payRecord", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("个人支付的监控");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this, Const.CHANNEL_ID);//

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

        helper = new DaoMaster.DevOpenHelper(this, Const.DB_NAME, null);
        daoMaster = new DaoMaster(helper.getWritableDatabase());

        AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
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
            Log.i(Const.TAG, "测试成功");
            Intent intent = new Intent();
            intent.setAction(Const.IntentAction);
            Uri uri = new Uri.Builder().scheme("app").path("log").query("msg=测试成功").build();
            intent.setData(uri);
            sendBroadcast(intent);
            playMedia(mediaPlayer);
            return;
        }
        String title = bundle.getString("android.title");
        String text = bundle.getString("android.text");
        Log.d(Const.TAG, "Notification posted [" + pkgName + "]:" + title + " & " + text);
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

                    BigDecimal actualPayAmount = new BigDecimal(money).setScale(2, RoundingMode.FLOOR);
                    new Thread(() -> {
                        postMethod(actualPayAmount.toPlainString(), uname);
                    }).start();
                    break;
                }
                Log.w(Const.TAG, "匹配失败" + text);
            } while (false);
        }
    }

    public void onNotificationRemoved(StatusBarNotification paramStatusBarNotification) {
        if (Build.VERSION.SDK_INT >= 19) {
            Bundle localObject = paramStatusBarNotification.getNotification().extras;
            String pkgName = paramStatusBarNotification.getPackageName();
            String title = localObject.getString("android.title");
            String text = (localObject).getString("android.text");
            Log.e(Const.TAG, "Notification removed [" + pkgName + "]:" + title + " & " + text);
        }
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        return START_NOT_STICKY;
    }

    public void postMethod(final String actualPayAmount, final String depositor) {
        //收到支付成功的系统通知后，找到本地记录的未完成的充值订单，把订单的状态修改了
        OrderEntity orderEntity = daoMaster.newSession().getOrderEntityDao().queryBuilder()
                .where(OrderEntityDao.Properties.Status.eq(充值订单状态_待支付))
                .where(OrderEntityDao.Properties.RechargeAmount.eq(actualPayAmount)).build().unique();

        if (orderEntity == null) {
            return;
        }

        playMedia(mediaPlayer);
        orderEntity.setActualDepositor(depositor);
        orderEntity.setActualPayAmount(actualPayAmount);
        orderEntity.setUpdate(System.currentTimeMillis());
        orderEntity.setStatus(充值订单状态_已支付);
        daoMaster.newSession().getOrderEntityDao().save(orderEntity);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");


        ApproveBean approveBean = new ApproveBean();
        approveBean.setActualPayAmount(actualPayAmount);
        approveBean.setCurrentAccountId(Const.MEMBER_ID);
        approveBean.setApprovalResult(充值订单状态_已支付);
        approveBean.setDeviceImei(AppUtil.getImei());
        approveBean.setId(orderEntity.getOrderId());

        String content = JSON.toJSONString(approveBean);
        String signature = CryptoUtil.sign(Const.PRIVATE_KEY, content);

        approveBean.setSignature(signature);

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(approveBean));
        Request request = new Request.Builder()
                .url(SERVER + "recharge/approval")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseString = response.body().string();
            Log.i(Const.TAG, "提交收款信息结果：" + responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playMedia(MediaPlayer media) {
        if (Const.PlaySounds) {
            media.start();
        }
    }
}