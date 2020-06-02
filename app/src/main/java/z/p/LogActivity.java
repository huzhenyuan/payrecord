package z.p;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import z.p.data.DaoMaster;
import z.p.data.OrderEntity;

public class LogActivity extends AppCompatActivity {
    private ListView container;
    private LogAdapter logAdapter;

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
        addLogItem();
    }

    private void addLogItem() {
        List<OrderEntity> orderData = daoMaster.newSession().getOrderEntityDao().loadAll();

        for (int i=0;i<100;i++) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId((long) i);
            orderEntity.setDepositor("setDepositor");
            orderEntity.setOrderId(String.valueOf(i));
            orderEntity.setActualDepositor("setActualDepositor");
            orderEntity.setActualPayAmount("100");
            orderEntity.setCreate(System.currentTimeMillis());
            orderEntity.setUpdate(System.currentTimeMillis());
            orderEntity.setRechargeAmount("200");
            orderData.add(orderEntity);
        }

        logAdapter = new LogAdapter(this, orderData);
        container.setAdapter(logAdapter);
        logAdapter.notifyDataSetChanged();
    }
}
