package z.p;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import z.p.data.OrderEntity;

public class LogActivity extends AppCompatActivity {
    private ListView container;
    private LogAdapter logAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        container = findViewById(R.id.log_container);

        Button btn_clear_log = findViewById(R.id.btn_clear_log);
        btn_clear_log.setEnabled(false);
        btn_clear_log.setOnClickListener(view -> {
            new Thread(() -> {
                MyApplication.getDaoSession().getOrderEntityDao().deleteAll();
            }).start();
            finish();
        });

        Button btn_refresh = findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(view -> {
            addLogItem();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        addLogItem();
    }

    private void addLogItem() {
        List<OrderEntity> orderData = MyApplication.getDaoSession().getOrderEntityDao().loadAll();
        List<OrderEntity> newOrderData = new LinkedList<>();

        for (OrderEntity orderDatum : orderData) {
            newOrderData.add(0, orderDatum);
        }
        logAdapter = new LogAdapter(this, newOrderData);
        container.setAdapter(logAdapter);
        logAdapter.notifyDataSetChanged();
    }
}
