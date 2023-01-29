package com.clarity.emailsms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_sms")
public class UserSMSEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sms_id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "message")
    private String message;
    @Column(name="phone_number")
    private String phoneNumber;
    @Column(name = "module")
    private String module;
    @Column(name = "action")
    private String action;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usersSMS", cascade = CascadeType.ALL)
    List<ResultSendEntity> resultSends= new ArrayList<>();

}
