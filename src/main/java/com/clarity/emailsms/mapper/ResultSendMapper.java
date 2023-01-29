package com.clarity.emailsms.mapper;

import com.clarity.emailsms.entity.ResultSendEntity;
import com.clarity.emailsms.dto.ResultSendDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ResultSendMapper {

    ResultSendDTO toResultSendDTO(ResultSendEntity resultSendEntity);
    ResultSendEntity toResultSendEntity(ResultSendDTO resultSendDTO);
}
