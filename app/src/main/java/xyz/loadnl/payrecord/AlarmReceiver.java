package xyz.loadnl.payrecord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.loadnl.payrecord.util.AppUtil;
import xyz.loadnl.payrecord.util.CryptoUtil;

import static xyz.loadnl.payrecord.AppConst.SERVER;

public class AlarmReceiver extends BroadcastReceiver {



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleEvent(MessageEvent messageEvent) {
        if (messageEvent.isHasImei()) {
            btn_imei.setVisibility(View.GONE);
            tv_device_imei.setEnabled(false);
            tv_device_imei.setText(messageEvent.getMessage());
            AppUtil.setImei(messageEvent.getMessage());
        } else {
            Toast.makeText(this, "请设置IMEI", Toast.LENGTH_LONG).show();
        }
    }


    // TODO 循环检查有没有分配给当前设备的订单
    // 如果有， 本地存下来，然后通知服务器就绪
    // 如果没有，服务器记录设备在线的信息
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(AppConst.TAG, "检查支付请求");
        new Thread(this::sendRequest).start();
        Intent startIntent = new Intent(context, AlarmService.class);
        context.startService(startIntent);
    }

    //检查有没有分配给当前设备的订单
    private void sendRequest() {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject orderQueryJson = new JSONObject();
        try {
            orderQueryJson.put("currentAccountId", AppConst.MEMBER_ID);
            orderQueryJson.put(AppConst.IMEI_KEY, AppUtil.getImei());

            String content = orderQueryJson.toString();
            String signature = CryptoUtil.sign(AppConst.PRIVATE_KEY, content);

            orderQueryJson.put("signature", signature);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(mediaType, orderQueryJson.toString());
        Request request = new Request.Builder()
                .url(SERVER + "recharge/approval")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseString = response.body().string();
            Log.i(AppConst.TAG, "提交收款信息结果：" + responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
