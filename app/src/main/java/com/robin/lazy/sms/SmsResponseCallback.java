package com.robin.lazy.sms;


public interface SmsResponseCallback {
    void onCallbackSmsContent(String smsId, String phoneNumber, String smsContent);
}
