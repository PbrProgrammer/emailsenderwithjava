package com.clarity.emailsms.kafka.dto;

import com.clarity.emailsms.kafka.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferKafkaDTO  {

    private String moduleName;
    private String actionName;
    private Object object;
    private Status status;
}
