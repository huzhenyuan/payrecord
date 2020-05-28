package z.p;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import z.p.data.DaoMaster;
import z.p.data.OrderEntity;

public class LogActivity extends AppCompatActivity {
    private LinearLayout container;

    private DaoMaster.DevOpenHelper helper;
    private DaoMaster daoMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        container = findViewById(R.id.log_container);
        helper = new DaoMaster.DevOpenHelper(this, Const.DB_NAME, null);
        daoMaster = new DaoMaster(helper.getWritableDatabase());

        Button btn_clear_log = findViewById(R.id.btn_clear_log);
        btn_clear_log.setOnClickListener(view -> {
            new Thread(() -> {
                daoMaster.newSession().getOrderEntityDao().deleteAll();
            }).start();
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        container.removeAllViews();
        addLogItem();
    }

    private void addLogItem() {
        List<OrderEntity> orderData = daoMaster.newSession().getOrderEntityDao().loadAll();
        for (OrderEntity orderDatum : orderData) {
            View view = View.inflate(this, R.layout.sample_log_item_comp, null);
            updateText(orderDatum, view);
            container.addView(view);
        }
    }

    private void updateText(OrderEntity data, View view) {
        TextView textView = view.findViewById(R.id.text_id);
        textView.setText(String.valueOf(data.getId()));
        textView = view.findViewById(R.id.text_time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        textView.setText(sdf.format(new Date(data.getCreate())));
        textView = view.findViewById(R.id.text_depositor);
        textView.setText(data.getDepositor());
        textView = view.findViewById(R.id.text_money);
        textView.setText(data.getRechargeAmount());

        //
        textView = view.findViewById(R.id.text_update_time);
        if (data.getUpdate() > 0) {
            textView.setText(sdf.format(new Date(data.getUpdate())));
        }

        textView = view.findViewById(R.id.text_actual_depositor);
        textView.setText(data.getActualDepositor());

        textView = view.findViewById(R.id.text_actual_money);
        textView.setText(data.getActualPayAmount());
    }
}
