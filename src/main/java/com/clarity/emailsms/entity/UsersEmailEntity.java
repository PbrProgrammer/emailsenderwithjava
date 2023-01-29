package com.clarity.emailsms.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_email")
public class UsersEmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "email_id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "mail_to")
    private String mailTo;
    @Column(name = "subject")
    private String subject;
    @Column(name = "message")
    private String message;
    @Column(name = "module")
    private String module;
    @Column(name = "action")
    private String action;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEmail", cascade = CascadeType.ALL)
    @Column(name = "resultSends")
    List<ResultSendEntity> resultSends= new ArrayList<>();

}
