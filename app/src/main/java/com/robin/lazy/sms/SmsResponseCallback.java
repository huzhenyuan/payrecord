package com.robin.lazy.sms;


public interface SmsResponseCallback {
    void onCallbackSmsContent(String phone, String smsContent);
}
