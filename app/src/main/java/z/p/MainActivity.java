package z.p;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import z.p.data.DaoMaster;
import z.p.data.OrderEntity;
import z.p.data.OrderEntityDao;
import z.p.data.PhoneInfoEntity;
import z.p.event.MessageEvent;
import z.p.event.NetworkEvent;
import z.p.util.AppUtil;

import static z.p.Const.充值订单状态_客户端停止接单;
import static z.p.Const.充值订单状态_待支付;

public class MainActivity extends AppCompatActivity {

    private Switch notification_switch;
    private Switch payService_switch;
    private TextView tv_device_imei;
    private TextView tip;
    private Button btn_imei;

    private DaoMaster.DevOpenHelper helper;
    private DaoMaster daoMaster;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleMessageEvent(MessageEvent messageEvent) {
        if (messageEvent.isHasImei()) {
            btn_imei.setVisibility(View.GONE);
            tv_device_imei.setEnabled(false);
            tv_device_imei.setText(messageEvent.getMessage());
            AppUtil.setImei(messageEvent.getMessage());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        notification_switch = findViewById(R.id.notification_switch);
        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != enabledPrivileges) {
                    openNotificationListenSettings();
                }
            }
        });

        payService_switch = findViewById(R.id.pay_service_switch);
        payService_switch.setChecked(false);
        payService_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkStatus();
                } else {
                    new Thread(() -> {
                        List<OrderEntity> orderEntityList = daoMaster.newSession().getOrderEntityDao().queryBuilder()
                                .where(OrderEntityDao.Properties.Status.eq(充值订单状态_待支付))
                                .build().list();
                        for (OrderEntity orderEntity : orderEntityList) {
                            orderEntity.setStatus(充值订单状态_客户端停止接单);
                        }
                    }).start();
                }
            }
        });


        Button btn_log = findViewById(R.id.btn_log);
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LogActivity.class);
                startActivity(intent);
            }
        });

        toggleNotificationListenerService();

        checkStatus();

        tv_device_imei = findViewById(R.id.tv_device_imei);
        btn_imei = findViewById(R.id.btn_imei);
        btn_imei.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(tv_device_imei.getText())) {
                PhoneInfoEntity phoneData = new PhoneInfoEntity();
                phoneData.setK(Const.IMEI_KEY);
                phoneData.setV(tv_device_imei.getText().toString());
                daoMaster.newSession().getPhoneInfoEntityDao().insert(phoneData);

                MessageEvent event = new MessageEvent();
                event.setHasImei(true);
                event.setMessage(tv_device_imei.getText().toString());
                EventBus.getDefault().post(event);
            }
        });
        tip = findViewById(R.id.tip);

        Intent startIntent = new Intent(this, AlarmService.class);
        startService(startIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent stopIntent = new Intent(this, AlarmService.class);
        stopService(stopIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStatus();
        EventBus.getDefault().register(this);
        new Thread(() -> {

            helper = new DaoMaster.DevOpenHelper(this, Const.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());

            List<PhoneInfoEntity> phoneDataList = daoMaster.newSession().getPhoneInfoEntityDao().loadAll();
            for (PhoneInfoEntity phoneData : phoneDataList) {
                if (TextUtils.equals(Const.IMEI_KEY, phoneData.getK())) {
                    MessageEvent event = new MessageEvent();
                    event.setHasImei(!TextUtils.isEmpty(phoneData.getV()));
                    event.setMessage(phoneData.getV());
                    EventBus.getDefault().post(event);
                }
            }
        }).start();
    }

    private boolean enabledPrivileges;

    private void checkStatus() {
        //权限开启.才能启动服务
        boolean enabled = isEnabled();
        enabledPrivileges = enabled;
        notification_switch.setChecked(enabled);
        if (!enabled) {
            payService_switch.setEnabled(false);
            return;
        }
        payService_switch.setEnabled(true);
        //开启服务
        ComponentName name = startService(new Intent(this, NotificationMonitorService.class));
        if (name == null) {
            payService_switch.setChecked(false);
            Toast.makeText(getApplicationContext(), "服务开启失败", Toast.LENGTH_LONG).show();
            return;
        }
        // 手动关闭服务之后 需要重新设置服务 所以在onCreate处调用
        // toggleNotificationListenerService();
        payService_switch.setChecked(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            keyCode = KeyEvent.KEYCODE_HOME;
        }
        return super.onKeyDown(keyCode, event);
    }

    //检查是否启用了监听通知的使用权
    private boolean isEnabled() {
        String str = getPackageName();
        String localObject = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(localObject)) {
            String[] strArr = (localObject).split(":");
            for (String name : strArr) {
                ComponentName localComponentName = ComponentName.unflattenFromString(name);
                if ((localComponentName != null) && (TextUtils.equals(str, localComponentName.getPackageName())))
                    return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void toggleNotificationListenerService() {
        PackageManager localPackageManager = getPackageManager();
        localPackageManager.setComponentEnabledSetting(new ComponentName(this, NotificationMonitorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        localPackageManager.setComponentEnabledSetting(new ComponentName(this, NotificationMonitorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * 打开通知权限设置.一般手机根本找不到哪里设置
     */
    private void openNotificationListenSettings() {
        try {
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}