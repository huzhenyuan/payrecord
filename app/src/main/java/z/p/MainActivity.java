package z.p;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.king.app.dialog.AppDialog;
import com.king.app.dialog.AppDialogConfig;
import com.king.app.updater.AppUpdater;
import com.robin.lazy.sms.SmsObserver;
import com.robin.lazy.sms.SmsResponseCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import z.p.data.OrderEntity;
import z.p.data.OrderEntityDao;
import z.p.data.PhoneInfoEntity;
import z.p.data.SmsEntity;
import z.p.data.SmsEntityDao;
import z.p.event.NetworkEvent;
import z.p.event.SmsEvent;
import z.p.event.UpdateEvent;
import z.p.model.SimpleResponse;
import z.p.model.SmsBean;
import z.p.util.AppUtil;
import z.p.util.CryptoUtil;
import z.p.util.LogcatUtil;

import static z.p.Const.SERVER;
import static z.p.Const.充值订单状态_客户端停止接单;
import static z.p.Const.充值订单状态_已支付;
import static z.p.Const.充值订单状态_待支付;

public class MainActivity extends AppCompatActivity implements SmsResponseCallback {

    Switch payService_switch;
    TextView tv_device_imei;
    Button btn_imei;
    TextView tip;

    SmsObserver smsObserver;

    public void updateImeiStatus(String imei) {
        if (!TextUtils.isEmpty(imei)) {
            btn_imei.setVisibility(View.GONE);
            tv_device_imei.setEnabled(false);
            tv_device_imei.setText(imei);
            AppUtil.setImei(imei);
        } else {
            Toast.makeText(this, "请设置IMEI", Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleNetworkEvent(NetworkEvent networkEvent) {
        if (networkEvent.isConnect()) {
            if (networkEvent.isWorking()) {
                tip.setBackgroundColor(Color.YELLOW);
            } else {
                tip.setBackgroundColor(Color.GREEN);
            }
        } else {
            tip.setBackgroundColor(Color.RED);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleUpdateEvent(UpdateEvent updateEvent) {

        int localVersionCode = AppUtil.getVersionCode(getApplicationContext());
        if (localVersionCode >= updateEvent.getVersionCode()) {
            return;
        }

        AppDialogConfig config = new AppDialogConfig();
        config.setTitle("PayRecord升级")
                .setOk("确认")
                .setContent("升级软件")
                .setOnClickOk(v -> {
                    new AppUpdater.Builder()
                            .serUrl(SERVER + "update/app")
                            .setFilename("ParRecord.apk")
                            .build(MainActivity.this)
                            .start();
                    AppDialog.INSTANCE.dismissDialog();
                });
        AppDialog.INSTANCE.showDialog(MainActivity.this, config);
    }

    void stopAllOrder() {
        List<OrderEntity> orderEntityList = MyApplication.getDaoSession().getOrderEntityDao().queryBuilder()
                .where(OrderEntityDao.Properties.Status.eq(充值订单状态_待支付))
                .build().list();
        for (OrderEntity orderEntity : orderEntityList) {
            orderEntity.setStatus(充值订单状态_客户端停止接单);
            MyApplication.getDaoSession().getOrderEntityDao().save(orderEntity);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        payService_switch = findViewById(R.id.pay_service_switch);
        payService_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (smsObserver == null) {
                Toast.makeText(getApplicationContext(), "没有获取到短信读取权限，退出程序", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (isChecked) {
                smsObserver.registerSMSObserver();
                MyApplication.working = true;
            } else {
                smsObserver.unregisterSMSObserver();
                MyApplication.working = false;
                new Thread(this::stopAllOrder).start();
            }
        });
        payService_switch.setChecked(false);

        Button btn_log = findViewById(R.id.btn_log);
        btn_log.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LogActivity.class);
            startActivity(intent);
        });

        Button btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(view -> new Thread(() -> Updater.checkUpdate(getApplicationContext())).start());

        tv_device_imei = findViewById(R.id.tv_device_imei);
        btn_imei = findViewById(R.id.btn_imei);
        btn_imei.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(tv_device_imei.getText())) {
                PhoneInfoEntity phoneInfoEntity = new PhoneInfoEntity();
                phoneInfoEntity.setK(Const.IMEI_KEY);
                phoneInfoEntity.setV(tv_device_imei.getText().toString());
                MyApplication.getDaoSession().getPhoneInfoEntityDao().insert(phoneInfoEntity);

                updateImeiStatus(tv_device_imei.getText().toString());
            }
        });

        tip = findViewById(R.id.tip);

        TextView info = findViewById(R.id.info);
        StringBuilder sb = new StringBuilder();
        sb.append("VERSION:")
                .append(AppUtil.getVersionName(this))
                .append(System.lineSeparator())
                .append(new Date().toString())
                .append(Const.SERVER.replace(".", "-")
                        .replace("/", "-")
                        .replace(":", "-")
                        .replace("http", ""))
                .append(System.lineSeparator())
                .append(Const.MEMBER_ID)
                .append(System.lineSeparator());
        info.setText(sb.toString());

        new Thread(this::stopAllOrder).start();


        Intent startIntent = new Intent(this, AlarmService.class);
        startService(startIntent);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent stopIntent = new Intent(this, AlarmService.class);
        stopService(stopIntent);

        EventBus.getDefault().unregister(this);
        if (smsObserver != null) {
            smsObserver.unregisterSMSObserver();
        }

        MyApplication.working = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<PhoneInfoEntity> phoneInfoEntities = MyApplication.getDaoSession().getPhoneInfoEntityDao().loadAll();
        for (PhoneInfoEntity phoneInfoEntity : phoneInfoEntities) {
            if (TextUtils.equals(phoneInfoEntity.getK(), Const.IMEI_KEY)) {
                updateImeiStatus(phoneInfoEntity.getV());
            }
        }

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_SMS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        smsObserver = new SmsObserver(MainActivity.this, MainActivity.this);
                        smsObserver.registerSMSObserver();
                        payService_switch.setChecked(true);
                        MyApplication.working = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        MyApplication.working = false;
                        Toast.makeText(getApplicationContext(), "无法读取短信，退出程序", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    }
                }).check();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            keyCode = KeyEvent.KEYCODE_HOME;
        }
        return super.onKeyDown(keyCode, event);
    }

    public synchronized void onCallbackSmsContent(final String smsId, final String phoneNumber, final String smsContent) {
        SmsEntity findSmsEntity = MyApplication.getDaoSession().getSmsEntityDao().queryBuilder().where(
                SmsEntityDao.Properties.SmsId.eq(smsId)
        ).unique();
        if (findSmsEntity != null) {
            return;
        }
        SmsEntity smsEntity = new SmsEntity();
        smsEntity.setSmsId(smsId);
        smsEntity.setPhoneNumber(phoneNumber);
        smsEntity.setSmsContent(smsContent);
        MyApplication.getDaoSession().getSmsEntityDao().save(smsEntity);

        SmsEvent smsEvent = new SmsEvent();
        smsEvent.setPhoneNumber(phoneNumber);
        smsEvent.setSmsContent(smsContent);
        EventBus.getDefault().post(smsEvent);
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onHandleSmsEvent(final SmsEvent smsEvent) {
        LogcatUtil.inst.d(Const.TAG, "准备回传 " + smsEvent);
        if (!Const.BANK_PHONE.containsKey(smsEvent.getPhoneNumber())) {
            return;
        }
        //收到支付成功的系统通知后，找到本地记录的未完成的充值订单，把订单的状态修改了
        OrderEntity orderEntity = MyApplication.getDaoSession().getOrderEntityDao().queryBuilder()
                .where(OrderEntityDao.Properties.Status.eq(充值订单状态_待支付))
                .build().unique();

        if (orderEntity == null) {
            LogcatUtil.inst.d(Const.TAG, "本地找不到待回传的订单记录");
            return;
        }

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        SmsBean smsBeanPo = new SmsBean();
        smsBeanPo.setCurrentAccountId(Const.MEMBER_ID);
        smsBeanPo.setDeviceImei(AppUtil.getImei());
        smsBeanPo.setOrderId(orderEntity.getOrderId());
        smsBeanPo.setSmsContent(smsEvent.getSmsContent());
        smsBeanPo.setPhoneNumber(smsEvent.getPhoneNumber());

        String poContent = JSON.toJSONString(smsBeanPo);
        String signature = CryptoUtil.sign(Const.PRIVATE_KEY, poContent);

        smsBeanPo.setSignature(signature);

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(smsBeanPo));
        Request request = new Request.Builder()
                .url(SERVER + "recharge/sms")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseString = response.body().string();


            SimpleResponse resp = JSON.parseObject(responseString, SimpleResponse.class);
            if (resp.isSuccess()) {
                orderEntity.setUpdate(System.currentTimeMillis());
                orderEntity.setSmsContent(smsEvent.getSmsContent());
                orderEntity.setStatus(充值订单状态_已支付);
                MyApplication.getDaoSession().getOrderEntityDao().save(orderEntity);
                LogcatUtil.inst.i(Const.TAG, "回传订单成功：" + responseString);
            }
            response.body().close();
        } catch (Exception e) {
            e.printStackTrace();
            LogcatUtil.inst.i(Const.TAG, "回传订单失败：" + e.toString());
        }
    }
}