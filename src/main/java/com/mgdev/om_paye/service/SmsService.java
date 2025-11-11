package com.mgdev.om_paye.service;

public interface SmsService {
    void sendOtpSms(String phoneNumber, String otpCode);
}