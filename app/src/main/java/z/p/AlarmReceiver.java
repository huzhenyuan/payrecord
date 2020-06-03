package z.p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import z.p.data.DaoMaster;
import z.p.data.OrderEntity;
import z.p.data.OrderEntityDao;
import z.p.event.NetworkEvent;
import z.p.model.ContentItem;
import z.p.model.Response;
import z.p.util.AppUtil;
import z.p.util.CryptoUtil;

import static z.p.Const.SERVER;
import static z.p.Const.充值订单状态_待支付;


public class AlarmReceiver extends BroadcastReceiver {

    private Context context;

    private DaoMaster.DevOpenHelper helper;
    private DaoMaster daoMaster;

    // TODO 清理老的订单
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (MainActivity.currentServiceSwitch.isChecked()) {
            helper = new DaoMaster.DevOpenHelper(context, Const.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
            new Thread(this::sendRequest).start();
        } else {
            Toast.makeText(context, "已停止检查订单", Toast.LENGTH_SHORT).show();
        }

        Intent startIntent = new Intent(context, AlarmService.class);
        context.startService(startIntent);
    }

    //检查有没有分配给当前设备的订单
    private void sendRequest() {
        if (TextUtils.isEmpty(AppUtil.getImei())) {
            return;
        }
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
            Log.i(Const.TAG, "提交查询分配给当前设备的订单结果：" + responseString);
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

        DaoMaster.DevOpenHelper helper;
        DaoMaster daoMaster;
        helper = new DaoMaster.DevOpenHelper(context, Const.DB_NAME, null);
        daoMaster = new DaoMaster(helper.getWritableDatabase());


        NetworkEvent networkEvent = new NetworkEvent();
        networkEvent.setConnect(response.isSuccess());
        EventBus.getDefault().post(networkEvent);


        if (response.isSuccess()) {
            List<ContentItem> contentList = response.getData().getContent();

            for (ContentItem contentItem : contentList) {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderId(contentItem.getOrderNo());
                orderEntity.setCreate(System.currentTimeMillis());
                orderEntity.setDepositor(contentItem.getDepositor());
                orderEntity.setStatus(充值订单状态_待支付);
                BigDecimal rechargeAmount = new BigDecimal(contentItem.getRechargeAmount()).setScale(2, RoundingMode.FLOOR);
                orderEntity.setRechargeAmount(rechargeAmount.toPlainString());

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


}
