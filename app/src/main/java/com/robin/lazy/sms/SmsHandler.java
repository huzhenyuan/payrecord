package com.robin.lazy.sms;

import android.os.Handler;
import android.os.Message;

public class SmsHandler extends Handler {

    private SmsResponseCallback mCallback;

    SmsHandler(SmsResponseCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == SmsObserver.MSG_RECEIVED_CODE) {
            String[] smsInfos = (String[]) msg.obj;
            if (smsInfos != null && smsInfos.length == 3 && mCallback != null) {
                mCallback.onCallbackSmsContent(smsInfos[0], smsInfos[1], smsInfos[2]);
            }
        }
    }
}
