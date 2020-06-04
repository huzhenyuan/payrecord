package z.p;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.king.app.dialog.AppDialog;
import com.king.app.dialog.AppDialogConfig;
import com.king.app.updater.AppUpdater;

import org.greenrobot.eventbus.EventBus;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import z.p.event.UpdateEvent;
import z.p.model.SimpleResponse;
import z.p.util.AppUtil;

import static z.p.Const.SERVER;

public class Updater {

    public static void checkUpdate(Context context) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(SERVER + "update/version")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        okhttp3.Response response = null;
        int versionCode = 0;
        try {
            response = client.newCall(request).execute();
            String responseString = response.body().string();
            SimpleResponse resp = JSON.parseObject(responseString, SimpleResponse.class);

            response.body().close();
            response.close();

            versionCode = Integer.parseInt(resp.getData());

        } catch (Exception e) {
            Log.i(Const.TAG, "Failed to checkUpdate " + e.toString());
            e.printStackTrace();
        }

        EventBus.getDefault().post(new UpdateEvent(versionCode));
    }
}
