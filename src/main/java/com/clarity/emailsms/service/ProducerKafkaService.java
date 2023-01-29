package com.clarity.emailsms.service;


import lombok.extern.slf4j.Slf4j;
import com.clarity.emailsms.kafka.dto.TransferKafkaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


@Slf4j
@Component
public class ProducerKafkaService {

    private final KafkaTemplate<String, TransferKafkaDTO> kafkaTemplate;

    @Autowired
    public ProducerKafkaService(KafkaTemplate<String, TransferKafkaDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessage(TransferKafkaDTO result, String topicName) {
        ListenableFuture<SendResult<String, TransferKafkaDTO>> future = kafkaTemplate.send(topicName, result);

        future.addCallback(new ListenableFutureCallback<SendResult<String, TransferKafkaDTO>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Failed to send message to Kafka: {}", ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, TransferKafkaDTO> result) {
                log.info("Message sent successfully to Kafka: {}", topicName);
            }
        });
    }

}
