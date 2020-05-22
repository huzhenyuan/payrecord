package xyz.loadnl.payrecord;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import xyz.loadnl.payrecord.data.DaoMaster;
import xyz.loadnl.payrecord.data.OrderData;

public class LogActivity extends AppCompatActivity {
    private LinearLayout container;

    private DaoMaster.DevOpenHelper helper;
    private DaoMaster daoMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        container = findViewById(R.id.log_container);
        helper = new DaoMaster.DevOpenHelper(this, AppConst.DB_NAME, null);
        daoMaster = new DaoMaster(helper.getWritableDatabase());
    }

    @Override
    protected void onResume() {
        super.onResume();
        container.removeAllViews();
        addLogItem();
    }

    private void addLogItem() {
        List<OrderData> orderData = daoMaster.newSession().getOrderDataDao().loadAll();
        for (OrderData orderDatum : orderData) {
            View view = View.inflate(this, R.layout.sample_log_item_comp, null);
            updateText(orderDatum, view);
            container.addView(view);
        }
    }

    private void updateText(OrderData data, View view) {
        TextView textView = view.findViewById(R.id.text_id);
        textView.setText(String.valueOf(data.getId()));
        textView = view.findViewById(R.id.text_time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        textView.setText(sdf.format(new Date(data.getCreate())));
        textView = view.findViewById(R.id.text_depositor);
        textView.setText(data.getDepositor());
        textView = view.findViewById(R.id.text_money);
        textView.setText(data.getMoney());
    }
}
