package com.clarity.emailsms.service;

import com.clarity.emailsms.dto.ResultSendDTO;
import com.clarity.emailsms.entity.ResultSendEntity;
import com.clarity.emailsms.entity.UserSMSEntity;
import com.clarity.emailsms.entity.UsersEmailEntity;
import com.clarity.emailsms.kafka.dto.EmailKafkaDTO;
import com.clarity.emailsms.kafka.dto.SMSKafkaDTO;
import com.clarity.emailsms.kafka.enums.Status;
import com.clarity.emailsms.kafka.events.failedsend.FailedSend;
import com.clarity.emailsms.kafka.events.successsend.SuccessSend;
import com.clarity.emailsms.mapper.ResultSendMapper;
import com.clarity.emailsms.repository.UserSMSRepo;
import com.clarity.emailsms.repository.UsersEmailRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class EmailSMSLogicService {

    private static final String TOPIC_RESULT_SMS = "result-sms";
    private static final String TOPIC_RESULT_Email = "result-email";

    private final FailedSend failedSend;
    private final SuccessSend successSend;
    private final SMSSenderImpl smsSender;
    private final EmailSender emailSender;
    private final UsersEmailRepo usersEmailRepo;
    private final UserSMSRepo userSMSRepo;
    private final ResultSendMapper resultSendMapper;

    public EmailSMSLogicService(FailedSend failedSend, SuccessSend successSend, SMSSenderImpl smsSender, EmailSender emailSender, UsersEmailRepo usersEmailRepo, UserSMSRepo userSMSRepo, ResultSendMapper resultSendMapper) {
        this.failedSend = failedSend;
        this.successSend = successSend;
        this.smsSender = smsSender;
        this.emailSender = emailSender;
        this.usersEmailRepo = usersEmailRepo;
        this.userSMSRepo = userSMSRepo;
        this.resultSendMapper = resultSendMapper;
    }

    public ResultSendEntity processSuccessEmail(EmailKafkaDTO dto, String moduleName) {
        long repeat = 0; // Repeat sending email 3 times in case of error
        ResultSendDTO resultSendDTO = new ResultSendDTO();
        while (repeat < 3) {
            try {
                emailSender.sendSimpleMessage(dto);
                resultSendDTO.setStatus(Status.SUCCESS);
                resultSendDTO.setSendDate(new Date().getTime());
                resultSendDTO.setReceptor(dto.getEmailTo());
                resultSendDTO.setMessage(dto.getEmailSubject());
                resultSendDTO.setModuleFrom(moduleName);

                successSend.sendSuccess(resultSendDTO, TOPIC_RESULT_Email);
                repeat = 3;
            } catch (Exception e) {
                repeat++;
            }
        }
        return resultSendMapper.toResultSendEntity(resultSendDTO);
    }

    @Transactional
    public void saveDataBaseEmail(String mailTo, String subject, String message, String user, String action, String module, ResultSendEntity resultSend) {
        UsersEmailEntity usersEmail = new UsersEmailEntity();
        usersEmail.setMessage(message);
        usersEmail.setUsername(user);
        usersEmail.setAction(action);
        usersEmail.setModule(module);
        usersEmail.setMailTo(mailTo);
        usersEmail.setSubject(subject);

        resultSend.setUserEmail(usersEmail);
        usersEmail.getResultSends().add(resultSend);

        usersEmailRepo.save(usersEmail);
    }

    public ResultSendEntity processSuccessSMS(SMSKafkaDTO dto, String moduleName) {
        long repeat = 0; // Repeat sending sms 3 times in case of error
        ResultSendDTO resultSendDTO = new ResultSendDTO();
        while (repeat < 3) {
            try {
                smsSender.sendFromKafka(dto);
                resultSendDTO.setStatus(Status.SUCCESS);
                resultSendDTO.setSendDate(new Date().getTime());
                resultSendDTO.setReceptor(dto.getPhoneNumber());
                resultSendDTO.setMessage(dto.getMessage());
                resultSendDTO.setModuleFrom(moduleName);

                successSend.sendSuccess(resultSendDTO, TOPIC_RESULT_SMS);
                repeat = 3;
            }catch (Exception e){
                repeat++;
            }
        }
        return resultSendMapper.toResultSendEntity(resultSendDTO);
    }

    public ResultSendEntity processFail(Exception e, String moduleName, String topic) {
        ResultSendDTO resultSendDTO = new ResultSendDTO();
        resultSendDTO.setStatus(Status.FAIL);
        resultSendDTO.setSendDate(new Date().getTime());
        resultSendDTO.setMessage(e.getMessage());
        resultSendDTO.setModuleFrom(moduleName);

        failedSend.sendFailed(resultSendDTO, topic);
        return resultSendMapper.toResultSendEntity(resultSendDTO);
    }

    @Transactional
    public void saveDataBaseSMS(String phone, String message, String user, String action, String module, ResultSendEntity resultSend) {
        UserSMSEntity usersSMSEntity = new UserSMSEntity();
        usersSMSEntity.setMessage(message);
        usersSMSEntity.setUsername(user);
        usersSMSEntity.setAction(action);
        usersSMSEntity.setModule(module);
        usersSMSEntity.setPhoneNumber(phone);

        resultSend.setUsersSMS(usersSMSEntity);
        usersSMSEntity.getResultSends().add(resultSend);

        userSMSRepo.save(usersSMSEntity);
    }
}
