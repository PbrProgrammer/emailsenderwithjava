package com.clarity.emailsms.dto;

import com.clarity.emailsms.kafka.enums.Status;
import lombok.Data;

@Data
public class ResultSendDTO {
    private String message;
    private Status status;
    private String receptor;
    private Long sendDate;
    private String moduleFrom;
}
