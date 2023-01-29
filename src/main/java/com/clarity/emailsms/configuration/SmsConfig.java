package com.clarity.emailsms.configuration;

import com.kavenegar.sdk.KavenegarApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsConfig {

    @Value(value = "${sms.apikey}")
    static String apiKeySms;
    public static KavenegarApi apiKey= new KavenegarApi(apiKeySms);
}
