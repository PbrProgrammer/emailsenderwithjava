package com.clarity.emailsms.kafka.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailKafkaDTO {

    private String userName;
    private String emailTo;
    private String emailSubject;
    private String emailBody;
}
