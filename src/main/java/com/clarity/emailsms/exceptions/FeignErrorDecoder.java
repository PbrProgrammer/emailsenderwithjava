package com.clarity.emailsms.exceptions;

import com.clarity.cloud.common.exception.handler.MessageResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Reader;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        String message = null;
        Reader reader = null;
        try {
            reader = response.body().asReader();
            String result = CharStreams.toString(reader);
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            MessageResponse exceptionMessage = mapper.readValue(result,
                    MessageResponse.class);
            message = exceptionMessage.getMessages().isEmpty() ? "" : exceptionMessage.getMessages().get(0);
            if (message.isEmpty()) {
                ResponseExceptionDTO responseExceptionDTO = mapper.readValue(result,
                        ResponseExceptionDTO.class);
                message = responseExceptionDTO.getMessage();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return new HystrixBadRequestException(message);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ResponseExceptionDTO {
        private String timestamp;
        private String status;
        private String error;
        private String message;
        private String path;
    }

}
