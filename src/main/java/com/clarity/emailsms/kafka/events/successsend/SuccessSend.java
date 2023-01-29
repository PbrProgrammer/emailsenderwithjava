package com.clarity.emailsms.kafka.events.successsend;


import com.clarity.emailsms.kafka.dto.TransferKafkaDTO;
import com.clarity.emailsms.kafka.enums.Status;
import com.clarity.emailsms.service.ProducerKafkaService;
import com.clarity.emailsms.annotation.Success;
import com.clarity.emailsms.dto.ResultSendDTO;
import com.clarity.emailsms.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;


@Success
public class SuccessSend {


    private static final String SEND_SUCCESS = "SEND_SUCCESS";
    private final ProducerKafkaService producerKafkaService;


    @Autowired
    public SuccessSend(ProducerKafkaService producerKafkaService) {
        this.producerKafkaService = producerKafkaService;
    }

    public void sendSuccess(ResultSendDTO resultSendDTO, String topic) {
        TransferKafkaDTO dto = new TransferKafkaDTO();
        dto.setStatus(Status.SUCCESS);
        dto.setActionName(SEND_SUCCESS);
        dto.setModuleName(Constants.MODULE_NAME);
        dto.setObject(resultSendDTO);
        producerKafkaService.sendMessage(dto, topic);
    }

}
