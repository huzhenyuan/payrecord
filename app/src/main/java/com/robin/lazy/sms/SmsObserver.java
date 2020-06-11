package com.robin.lazy.sms;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

import z.p.Const;
import z.p.util.LogcatUtil;

public class SmsObserver extends ContentObserver {

    public static final int MSG_RECEIVED_CODE = 1001;
    private Context mContext;
    private SmsHandler mHandler;


    public SmsObserver(Activity context, SmsResponseCallback callback) {
        this(new SmsHandler(callback));
        this.mContext = context;
    }

    public SmsObserver(SmsHandler handler) {
        super(handler);
        this.mHandler = handler;
    }


    public void registerSMSObserver() {
        Uri uri = Uri.parse("content://sms");
        if (mContext != null) {
            mContext.getContentResolver().registerContentObserver(uri,
                    true, this);
        }
    }

    public void unregisterSMSObserver() {
        if (mContext != null) {
            mContext.getContentResolver().unregisterContentObserver(this);
        }
        if (mHandler != null) {
            mHandler = null;
        }
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        if (uri.toString().equals("content://sms/raw")) {
            return;
        }
        Uri inboxUri = Uri.parse("content://sms/inbox");//收件箱
        try {
            Cursor c = mContext.getContentResolver().query(inboxUri, null, null,
                    null, "date desc");
            if (c != null) {
                if (c.moveToFirst()) {
                    String address = c.getString(c.getColumnIndex("address"));
                    String body = c.getString(c.getColumnIndex("body"));
                    if (mHandler != null) {
                        mHandler.obtainMessage(MSG_RECEIVED_CODE, new String[]{address, body})
                                .sendToTarget();
                    }
                    LogcatUtil.inst.i(Const.TAG, "发件人为：" + address + " " + "短信内容为：" + body);
                }
                c.close();
            }
        } catch (SecurityException e) {
            LogcatUtil.inst.i(Const.TAG, "获取短信权限失败" + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
