package com.clarity.emailsms.entity;

import com.clarity.emailsms.kafka.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "result_send")
public class ResultSendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String message;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private String receptor;
    private Long sendDate;
    private String moduleFrom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email_id", referencedColumnName = "email_id")
    private UsersEmailEntity userEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_sms_id", referencedColumnName = "sms_id")
    private UserSMSEntity  usersSMS;

}
