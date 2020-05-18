package xyz.loadnl.payrecord;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.loadnl.payrecord.data.AppConst;
import xyz.loadnl.payrecord.data.OrderData;
import xyz.loadnl.payrecord.util.AppUtil;
import xyz.loadnl.payrecord.util.DBManager;

public class NotificationMonitorService extends NotificationListenerService {

    private static final String AliPay = "ALIPAY";
    private static final String WeixinPay = "WXPAY";

    public long lastTimePosted = System.currentTimeMillis();
    private Pattern pAlipay;
    private Pattern pAlipay2;
    private Pattern pAlipayDianyuan;
    private Pattern pWeixin;

    private MediaPlayer mediaPlayer;
    private PowerManager.WakeLock wakeLock;
    private DBManager dbManager;
    private BatteryReceiver batteryReceiver;


    public void onCreate() {
        super.onCreate();
        int version = AppUtil.getVersionCode(this);
        sendingList = new ArrayList<>();

        Toast.makeText(getApplicationContext(), "启动服务", Toast.LENGTH_LONG).show();
        //支付宝
        String pattern = "(\\S*)通过扫码向你付款([\\d\\.]+)元";
        pAlipay = Pattern.compile(pattern);
        pattern = "成功收款([\\d\\.]+)元。享免费提现等更多专属服务，点击查看";
        pAlipay2 = Pattern.compile(pattern);
        pAlipayDianyuan = Pattern.compile("支付宝成功收款([\\d\\.]+)元。收钱码收钱提现免费，赶紧推荐顾客使用");
        pWeixin = Pattern.compile("微信支付收款([\\d\\.]+)元");

        mediaPlayer = MediaPlayer.create(this, R.raw.payrecv);
        MediaPlayer payNetWorkError = MediaPlayer.create(this, R.raw.networkerror);
        dbManager = new DBManager(this);
//        if (AppConst.AppId < 1) {
//            String appid = dbManager.getConfig(AppConst.KeyAppId);
//            if (!TextUtils.isEmpty(appid)) {
//                AppConst.AppId = Integer.parseInt(appid);
//                String token = dbManager.getConfig(AppConst.KeyToken);
//                if (!TextUtils.isEmpty(token)) {
//                    AppConst.Token = token;
//                }
//                String secret = dbManager.getConfig(AppConst.KeySecret);
//                if (!TextUtils.isEmpty(secret)) {
//                    AppConst.Secret = secret;
//                }
//            }
//            // 推送前判断下playSounds
//            String mute = dbManager.getConfig(AppConst.KeyMute);
//            if(!TextUtils.isEmpty(mute)){
//                AppConst.PlaySounds = Boolean.parseBoolean(mute);
//            }
//        }
        Log.i("loadnl", "Notification Monitor Service start");
//        new Thread(this).start();

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

        nb.setContentTitle("PayRecord").setTicker("PayRecord个人支付").setSmallIcon(R.mipmap.ic_launcher);
        nb.setContentText("个人支付运行中.请保持此通知一直存在");
        //nb.setContent(new RemoteViews(getPackageName(),R.layout.layout));
        nb.setWhen(System.currentTimeMillis());
        Notification notification = nb.build();
        startForeground(1, notification);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //保持cpu一直运行，不管屏幕是否黑屏
        if (pm != null && wakeLock == null) {
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
            wakeLock.acquire(7 * 24 * 60 * 60 * 1000L /*10 minutes*/);
        }

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryReceiver = new BatteryReceiver();
        registerReceiver(batteryReceiver, intentFilter);
    }


    public void onDestroy() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
        Intent localIntent = new Intent();
        localIntent.setClass(this, NotificationMonitorService.class);
        startService(localIntent);
        unregisterReceiver(batteryReceiver);
    }


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
            //payRecv.start();
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
        //支付宝com.eg.android.AlipayGphone
        //com.eg.android.AlipayGphone]:支付宝通知 & 新哥通过扫码向你付款0.01元
        if (pkgName.equals("com.eg.android.AlipayGphone")) {
            // 现在创建 matcher 对象
            do {
                Matcher m = pAlipay.matcher(text);
                if (m.find()) {
                    String uname = m.group(1);
                    String money = m.group(2);
                    postMethod(AliPay, money, uname, false);
                    break;
                }
                m = pAlipay2.matcher(text);
                if (m.find()) {
                    String money = m.group(1);
                    postMethod(AliPay, money, "支付宝用户", false);
                    break;
                }
                m = pAlipayDianyuan.matcher(text);
                if (m.find()) {
                    String money = m.group(1);
                    postMethod(AliPay, money, "支付宝-店员", true);
                    break;
                }
                Log.w(AppConst.TAG, "匹配失败" + text);
            } while (false);
        }
        //微信
        //com.tencent.mm]:微信支付 & 微信支付收款0.01元
        else if (pkgName.equals("com.tencent.mm") && text != null) {
            // 现在创建 matcher 对象
            Matcher m = pWeixin.matcher(text);
            if (m.find()) {
                String uname = "微信用户";
                String money = m.group(1);
                postMethod(WeixinPay, money, uname, false);
            }
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

    private ArrayList<OrderData> sendingList;

    /**
     * 获取道的支付通知发送到服务器
     *
     * @param payType  支付方式
     * @param money    支付金额
     * @param username 支付者名字
     */
    public void postMethod(final String payType, final String money, final String username, boolean dianYuan) {
        dbManager.addLog("new order:" + payType + "," + money + "," + username, 101);
        playMedia(mediaPlayer);
//        String app_id = "" + AppConst.AppId;
//        String rndStr = AppUtil.randString(16);
//        OrderData data = new OrderData(payType, money, username, dianYuan);
//        String sign = AppUtil.toMD5(app_id + AppConst.Secret + data.time + version + rndStr + payType + money + username);
//        data.sign = sign;
//        data.ranStr = rndStr;
//        postMethod(data);
    }

//    private void postMethod(final OrderData data) {
//        if (data == null) {
//            return;
//        }
//        RequestUtils.getRequest(AppConst.HostUrl + "person/notify/pay?type=" + data.payType
//                        + "&money=" + data.money
//                        + "&uname=" + data.username
//                        + "&appid=" + "" + AppConst.AppId
//                        + "&rndstr=" + data.ranStr
//                        + "&sign=" + data.sign
//                        + "&time=" + data.time
//                        + "&dianyuan=" + data.dianYuan
//                        + "&version=" + version
//                , new IHttpResponse() {
//                    @Override
//                    public void OnHttpData(String data) {
//                        dbManager.addLog(data, 200);
//                        handleMessage(data, 1);
//                    }
//
//                    @Override
//                    public void OnHttpDataError(IOException e) {
//                        dbManager.addLog("http error," + e.getMessage(), 500);
//                        sendingList.add(data);
//                    }
//                });
//
//    }

    private long lastSendTime;

    /**
     * 发送错误信息到服务器
     */
//    public void postState() {
//        lastSendTime = System.currentTimeMillis();
//        Log.d(AppConst.TAG_LOG, "发送在线信息");
//        RequestUtils.getRequest(AppConst.authUrl("person/state/online") + "&v=" + AppConst.version + "&b=" + AppConst.Battery, new IHttpResponse() {
//            @Override
//            public void OnHttpData(String data) {
//                handleMessage(data, 3);
//            }
//
//            @Override
//            public void OnHttpDataError(IOException e) {
//                Log.w(AppConst.TAG_LOG,e);
//            }
//        });
//
//    }


    private long lastNetTime;

//    public boolean handleMessage(String message, int arg1) {
//        lastNetTime = System.currentTimeMillis();
//        if (message == null || message.isEmpty()) {
//            return true;
//        }
//        String msg = message;
//        Log.i(AppConst.TAG_LOG, msg);
//        JSONObject json;
//        try {
//            json = new JSONObject(msg);
//
//            if (json.getInt("code") != 0) {
//                String emsg = json.getString("msg");
//                Log.w(AppConst.TAG_LOG, emsg);
//                return false;
//            }
//            if (json.has("time")) {
//                int time = json.getInt("time");
//                int dt = (int) (System.currentTimeMillis() / 1000) - time;
//                AppConst.DetaTime = (dt + AppConst.DetaTime * 9) / 10;
//                Log.d(AppConst.TAG_LOG, "服务器时间差" + AppConst.DetaTime);
//            }
//            if (arg1 == 3) {
//                return true;
//            }
//            playMedia(payComp);
//
//        } catch (JSONException e) {
//            Log.w(AppConst.TAG_LOG, e);
//        }
//
//        return true;
//    }

    private void playMedia(MediaPlayer media) {
        if (AppConst.PlaySounds) {
            media.start();
        }
    }
}