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

import z.p.data.OrderEntity;
import z.p.data.OrderEntityDao;
import z.p.data.PhoneInfoEntity;
import z.p.event.NetworkEvent;
import z.p.event.UpdateEvent;
import z.p.util.AppUtil;

import static z.p.Const.SERVER;
import static z.p.Const.充值订单状态_客户端停止接单;
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
            tip.setBackgroundColor(Color.GREEN);
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
                new Thread(() -> {
                    List<OrderEntity> orderEntityList = MyApplication.getDaoSession().getOrderEntityDao().queryBuilder()
                            .where(OrderEntityDao.Properties.Status.eq(充值订单状态_待支付))
                            .build().list();
                    for (OrderEntity orderEntity : orderEntityList) {
                        orderEntity.setStatus(充值订单状态_客户端停止接单);
                    }
                }).start();
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


        Intent startIntent = new Intent(this, AlarmService.class);
        startService(startIntent);

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

    @Override
    public void onCallbackSmsContent(String phone, String smsContent) {

        //TODO 回传给服务器
    }
}