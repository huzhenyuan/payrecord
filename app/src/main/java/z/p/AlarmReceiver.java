package z.p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import z.p.data.OrderEntity;
import z.p.data.OrderEntityDao;
import z.p.event.NetworkEvent;
import z.p.model.ContentItem;
import z.p.model.Response;
import z.p.util.AppUtil;
import z.p.util.CryptoUtil;
import z.p.util.LogcatUtil;

import static z.p.Const.SERVER;
import static z.p.Const.充值订单状态_客户端停止接单;
import static z.p.Const.充值订单状态_待支付;
import static z.p.Const.充值订单状态_由于有新订单取消之前订单;


public class AlarmReceiver extends BroadcastReceiver {

    private static final AtomicLong index = new AtomicLong(0);
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (MyApplication.working) {
            new Thread(this::sendRequest).start();
        } else {
            Toast.makeText(context, "已停止接单", Toast.LENGTH_SHORT).show();
        }

        Intent startIntent = new Intent(context, AlarmService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(startIntent);
        } else {
            context.startService(startIntent);
        }

        if (index.getAndIncrement() % 3 == 0) {
            new Thread(() -> LogcatUtil.inst.upload(AppUtil.getImei(), AppUtil.getVersionName(context))).start();
        }
    }

    //检查有没有分配给当前设备的订单
    private void sendRequest() {
        if (TextUtils.isEmpty(AppUtil.getImei())) {
            return;
        }
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(5000, TimeUnit.SECONDS)
                .writeTimeout(5000, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject orderQueryJson = new JSONObject();
        try {
            orderQueryJson.put("currentAccountId", Const.MEMBER_ID);
            orderQueryJson.put(Const.IMEI_KEY, AppUtil.getImei());

            String content = orderQueryJson.toString();
            String signature = CryptoUtil.sign(Const.PRIVATE_KEY, content);

            orderQueryJson.put("signature", signature);
        } catch (JSONException e) {

            NetworkEvent networkEvent = new NetworkEvent();
            networkEvent.setConnect(false);
            EventBus.getDefault().post(networkEvent);

            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(mediaType, orderQueryJson.toString());
        Request request = new Request.Builder()
                .url(SERVER + "recharge/findMemberImeiRechargeOrderByPage")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseString = response.body().string();
            Response resp = JSON.parseObject(responseString, Response.class);

            response.body().close();
            response.close();

            onHandleEvent(resp);
        } catch (Exception e) {

            NetworkEvent networkEvent = new NetworkEvent();
            networkEvent.setConnect(false);
            EventBus.getDefault().post(networkEvent);

            e.printStackTrace();
        }
    }

    private void onHandleEvent(Response response) {
        NetworkEvent networkEvent = new NetworkEvent();
        networkEvent.setConnect(response.isSuccess());


        if (response.isSuccess()) {
            List<ContentItem> contentList = response.getData().getContent();

            for (ContentItem contentItem : contentList) {
                networkEvent.setWorking(true);

                //找找是否已经有这个订单
                OrderEntity localOrderEntity = MyApplication.getDaoSession().getOrderEntityDao().queryBuilder()
                        .where(OrderEntityDao.Properties.OrderId.eq(contentItem.getOrderNo())).unique();
                if (localOrderEntity == null) {
                    OrderEntity orderEntity = new OrderEntity();
                    orderEntity.setOrderId(contentItem.getOrderNo());
                    orderEntity.setCreate(System.currentTimeMillis());
                    orderEntity.setDepositor(contentItem.getDepositor());
                    orderEntity.setStatus(充值订单状态_待支付);
                    BigDecimal rechargeAmount = new BigDecimal(contentItem.getRechargeAmount()).setScale(2, RoundingMode.FLOOR);
                    orderEntity.setRechargeAmount(rechargeAmount.toPlainString());
                    MyApplication.getDaoSession().getOrderEntityDao().save(orderEntity);
                    LogcatUtil.inst.i(Const.TAG, "存储订单：" + JSON.toJSONString(orderEntity));
                } else {
                    if (TextUtils.equals(充值订单状态_客户端停止接单, localOrderEntity.getStatus())) {
                        localOrderEntity.setStatus(充值订单状态_待支付);
                        MyApplication.getDaoSession().getOrderEntityDao().save(localOrderEntity);
                        LogcatUtil.inst.i(Const.TAG, "已经存在订单：" + JSON.toJSONString(localOrderEntity));
                    }
                }

                List<OrderEntity> 其他待支付订单列表 = MyApplication.getDaoSession().getOrderEntityDao().queryBuilder()
                        .where(OrderEntityDao.Properties.OrderId.notEq(contentItem.getOrderNo()),
                                OrderEntityDao.Properties.Status.eq(充值订单状态_待支付))
                        .list();
                for (OrderEntity oldOrderEntity : 其他待支付订单列表) {
                    oldOrderEntity.setStatus(充值订单状态_由于有新订单取消之前订单);
                    MyApplication.getDaoSession().getOrderEntityDao().save(oldOrderEntity);
                }
            }
        }

        EventBus.getDefault().post(networkEvent);
    }


}
