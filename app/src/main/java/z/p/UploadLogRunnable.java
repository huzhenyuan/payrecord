package z.p;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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


public class UploadLogRunnable implements Runnable {

    private Context context;

    public UploadLogRunnable(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Log.i(OrderFetchRunnable.class.getSimpleName(), "上传日志");
        LogcatUtil.inst.upload(AppUtil.getImei(), AppUtil.getVersionName(context));
    }
}
