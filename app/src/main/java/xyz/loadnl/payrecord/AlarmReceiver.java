package xyz.loadnl.payrecord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import xyz.loadnl.payrecord.data.DaoMaster;
import xyz.loadnl.payrecord.data.OrderEntity;
import xyz.loadnl.payrecord.data.OrderEntityDao;
import xyz.loadnl.payrecord.event.OrderEvent;
import xyz.loadnl.payrecord.model.ContentItem;
import xyz.loadnl.payrecord.model.Response;
import xyz.loadnl.payrecord.util.AppUtil;
import xyz.loadnl.payrecord.util.CryptoUtil;

import static xyz.loadnl.payrecord.Const.SERVER;

public class AlarmReceiver extends BroadcastReceiver {


    private DaoMaster.DevOpenHelper helper;
    private DaoMaster daoMaster;


    public AlarmReceiver() {
        super();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleEvent(OrderEvent event) {
        if (helper == null) {
            return;
        }
        Response response = event.getResponse();
        if (response.isSuccess()) {
            List<ContentItem> contentList = response.getData().getContent();

            for (ContentItem contentItem : contentList) {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderId(contentItem.getOrderNo());
                orderEntity.setCreate(System.currentTimeMillis());
                orderEntity.setDepositor(contentItem.getDepositor());
                orderEntity.setMoney(Double.valueOf(contentItem.getRechargeAmount()).toString());

                OrderEntity findEntity = daoMaster.newSession().getOrderEntityDao().queryBuilder()
                        .where(OrderEntityDao.Properties.OrderId.eq(contentItem.getOrderNo())).unique();
                if (findEntity == null) {
                    daoMaster.newSession().getOrderEntityDao().save(orderEntity);
                    Log.i(Const.TAG, "存储订单：" + JSON.toJSONString(orderEntity));
                } else {
                    Log.i(Const.TAG, "已经存在订单：" + JSON.toJSONString(orderEntity));
                }
            }
        }
    }


    // TODO 循环检查有没有分配给当前设备的订单
    // 如果有， 本地存下来，然后通知服务器就绪
    // 如果没有，服务器记录设备在线的信息
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(Const.TAG, "检查支付请求");
        new Thread(this::sendRequest).start();
        Intent startIntent = new Intent(context, AlarmService.class);
        context.startService(startIntent);

        if (helper == null) {
            helper = new DaoMaster.DevOpenHelper(context, Const.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
    }

    //检查有没有分配给当前设备的订单
    private void sendRequest() {

        OkHttpClient client = new OkHttpClient().newBuilder()
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
            Log.i(Const.TAG, "提交查询分配给当前设备的订单结果：" + responseString);
            Response resp = JSON.parseObject(responseString, Response.class);

            response.body().close();
            response.close();
            OrderEvent event = new OrderEvent();
            event.setResponse(resp);
            EventBus.getDefault().post(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
