package com.clarity.emailsms.service;


import com.clarity.emailsms.kafka.dto.SMSKafkaDTO;
import com.clarity.emailsms.kafka.dto.TransferKafkaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import com.clarity.emailsms.kafka.dto.EmailKafkaDTO;
import com.clarity.emailsms.entity.ResultSendEntity;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ConsumerKafkaService {

    private static final String TOPIC_RESULT_SMS = "result-sms";
    private static final String TOPIC_RESULT_Email = "result-email";
    private final EmailSMSLogicService emailSMSLogicService;


    @Autowired
    public ConsumerKafkaService(EmailSMSLogicService emailSMSLogicService) {
        this.emailSMSLogicService = emailSMSLogicService;
    }

    @KafkaListener(topics = "notification-to-sms", containerFactory = "KafkaListenerContainerFactory")
    public void consumeSMS(ConsumerRecord<String, TransferKafkaDTO> transferDto) {
        TransferKafkaDTO tkd = transferDto.value();
        ResultSendEntity resultSend;
        try {
            SMSKafkaDTO dto = new ObjectMapper().readValue(gsonConvertor(transferDto), SMSKafkaDTO.class);
            resultSend = emailSMSLogicService.processSuccessSMS(dto, tkd.getModuleName());
            emailSMSLogicService.saveDataBaseSMS(dto.getPhoneNumber(), dto.getMessage(), dto.getUserName(),
                    tkd.getActionName(), tkd.getModuleName(), resultSend);
        } catch (Exception e) {
            resultSend = emailSMSLogicService.processFail(e, tkd.getModuleName(), TOPIC_RESULT_SMS);
            emailSMSLogicService.saveDataBaseSMS(null, null, null,
                    tkd.getActionName(), tkd.getModuleName(), resultSend);
        }

    }


    @KafkaListener(topics = "notification-to-email", containerFactory = "KafkaListenerContainerFactory")
    public void consumeEmail(ConsumerRecord<String, TransferKafkaDTO> transferDto) {
        TransferKafkaDTO tkd = transferDto.value();
        ResultSendEntity resultSend;
        try {
            EmailKafkaDTO dto = new ObjectMapper().readValue(gsonConvertor(transferDto), EmailKafkaDTO.class);
            resultSend = emailSMSLogicService.processSuccessEmail(dto, tkd.getModuleName());
            emailSMSLogicService.saveDataBaseEmail(dto.getEmailSubject(), dto.getEmailTo(), dto.getEmailBody(), dto.getUserName(),
                    tkd.getActionName(), tkd.getModuleName(), resultSend);


        } catch (Exception e) {
            resultSend = emailSMSLogicService.processFail(e, transferDto.value().getModuleName(), TOPIC_RESULT_Email);
            emailSMSLogicService.saveDataBaseEmail(null, null, null, null,
                    tkd.getActionName(), tkd.getModuleName(), resultSend);
        }
    }

    private String gsonConvertor(ConsumerRecord<String, TransferKafkaDTO> transferDto) {
        Gson gson = new Gson();
        return gson.toJson(transferDto.value().getObject());
    }
    






}
