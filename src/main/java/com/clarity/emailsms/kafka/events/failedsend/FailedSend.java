package com.clarity.emailsms.kafka.events.failedsend;


import com.clarity.emailsms.annotation.Failure;
import com.clarity.emailsms.kafka.dto.TransferKafkaDTO;
import com.clarity.emailsms.kafka.enums.Status;
import com.clarity.emailsms.service.ProducerKafkaService;
import com.clarity.emailsms.dto.ResultSendDTO;
import com.clarity.emailsms.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;


@Failure
public class FailedSend {


    private static final String SEND_FAILED = "SEND_FAILED";

    private final ProducerKafkaService producerKafkaService;

    @Autowired
    public FailedSend(ProducerKafkaService producerKafkaService) {
        this.producerKafkaService = producerKafkaService;
    }

    public void sendFailed(ResultSendDTO resultSendDTO, String topic) {
        TransferKafkaDTO dto = new TransferKafkaDTO();
        dto.setStatus(Status.FAIL);
        dto.setActionName(SEND_FAILED);
        dto.setModuleName(Constants.MODULE_NAME);
        dto.setObject(resultSendDTO);
        producerKafkaService.sendMessage(dto, topic);
    }

}
