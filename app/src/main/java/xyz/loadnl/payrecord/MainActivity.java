package xyz.loadnl.payrecord;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import xyz.loadnl.payrecord.util.AppUtil;

public class MainActivity extends AppCompatActivity {

    private Switch notification_switch;
    private Switch payService_switch;
    private TextView tv_device_id;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUtil.initDeviceId(this);

        TextView tv_device_id = findViewById(R.id.tv_device_id);
        tv_device_id.setText(getString(R.string.device_id) + AppUtil.getDeviceId());


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

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStatus();
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
    protected void onDestroy() {
        super.onDestroy();
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