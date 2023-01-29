package com.clarity.emailsms.service;



import com.clarity.emailsms.configuration.SmsConfig;
import com.clarity.emailsms.exceptions.ClarityException;
import com.clarity.emailsms.kafka.dto.SMSKafkaDTO;
import com.kavenegar.sdk.excepctions.ApiException;
import com.kavenegar.sdk.excepctions.HttpException;
import com.kavenegar.sdk.models.SendResult;
import org.springframework.stereotype.Service;

@Service
public class SMSSenderImpl {


    public SendResult sendFromKafka(SMSKafkaDTO messageDto) {
        try {
          return SmsConfig.apiKey.send("", messageDto.getPhoneNumber(), messageDto.getMessage());
        }
        catch (HttpException ex)
        {
            throw new ClarityException.BadRequest("HttpException  : " + ex.getMessage());
        }
        catch (ApiException ex)
        {
            throw new ClarityException.BadRequest("ApiException : " + ex.getMessage());
        }
    }

    public SendResult sendFromRest(SMSKafkaDTO messageDto) {
        try {
            return SmsConfig.apiKey.send("", messageDto.getPhoneNumber(), messageDto.getMessage());
        }
        catch (HttpException ex)
        {
            throw new ClarityException.BadRequest("HttpException  : " + ex.getMessage());
        }
        catch (ApiException ex)
        {
            throw new ClarityException.BadRequest("ApiException : " + ex.getMessage());
        }
    }
}
