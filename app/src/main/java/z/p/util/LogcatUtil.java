package z.p.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import z.p.model.Response;

import static z.p.Const.SERVER;

public class LogcatUtil {

    public static final LogcatUtil inst = new LogcatUtil();
    private StringBuffer sb = new StringBuffer();

    private LogcatUtil() {
    }

    public void init(Context context) {
    }

    public void d(String TAG, String message) {
        sb.append("D");
        sb.append("-").append(new Date().getTime());
        sb.append("-").append(TAG);
        sb.append(" ").append(message).append(System.lineSeparator());
        Log.d(TAG, message);
    }

    public void i(String TAG, String message) {
        sb.append("I");
        sb.append("-").append(new Date().getTime());
        sb.append("-").append(TAG);
        sb.append(" ").append(message).append(System.lineSeparator());
        Log.i(TAG, message);
    }

    public void e(String TAG, String message) {
        sb.append("E");
        sb.append("-").append(new Date().getTime());
        sb.append("-").append(TAG);
        sb.append(" ").append(message).append(System.lineSeparator());
        Log.e(TAG, message);
    }

    public void upload(String imei, String versionName) {
        String content = sb.toString();
        sb = new StringBuffer();
        if (TextUtils.isEmpty(imei)) {
            return;
        }
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, imei + "\t" + versionName + "\n" + content);
        Request request = new Request.Builder()
                .url(SERVER + "logcat/upload")
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
