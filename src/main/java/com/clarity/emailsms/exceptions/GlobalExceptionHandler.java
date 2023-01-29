package com.clarity.emailsms.exceptions;

import com.clarity.cloud.common.data.error.ErrorDTO;
import com.clarity.cloud.common.exception.AppException;
import com.clarity.emailsms.component.MessageByLocaleComponent;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.SQLGrammarException;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	private static final String LOAD_BALANCER_ERROR = "Load balancer does not have";
	private static final String SSO_SERVICE = "SsoService";
	private static final String ERROR = "Error";
	private final MessageByLocaleComponent messageByLocaleComponent;

	@Autowired
	public GlobalExceptionHandler(MessageByLocaleComponent messageByLocaleComponent) {
		this.messageByLocaleComponent = messageByLocaleComponent;
	}


	@ExceptionHandler(AppException.class)
	public ResponseEntity<Object> handleException(AppException appException) {
		return handleAppException(appException);
	}

	private ResponseEntity<Object> handleAppException(AppException appException) {
		ErrorDTO response = new ErrorDTO();
		if (appException.getMessages() != null) {
			response.setMessage(appException.getMessages().toString());
		} else if (appException.getMessage() != null)
			response.setMessage(appException.getMessage());
		else {
			response.setMessage(AppException.getGeneralMessage());
		}

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({HttpMessageNotReadableException.class})
	public ResponseEntity<ErrorDTO> httpMessageNotReadableException(Exception exception, WebRequest request) {
		log.info(exception.getMessage(), exception);
		String message = "Invalid value";
		return new ResponseEntity<>(createResponse(HttpStatus.BAD_REQUEST, message, request), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler({SQLGrammarException.class, BadSqlGrammarException.class, PSQLException.class, SQLException.class})
	public ResponseEntity<ErrorDTO> sqlException(SQLGrammarException exception, WebRequest request) {
		log.info(exception.getMessage(), exception);
		String message = messageByLocaleComponent.getMessage("internal.server.server.err.msg");
		return new ResponseEntity<>(createResponse(INTERNAL_SERVER_ERROR, message, request), HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<ErrorDTO> methodArgumentNotValidException(Exception exception, WebRequest request) {
		log.info(exception.getMessage(), exception);
		String message = "";
		message = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors().stream().map(fieldError -> {
					String errorMessage = fieldError.getField() + " in " + fieldError.getObjectName() + " " + fieldError.getDefaultMessage();
					log.info(errorMessage);
					return errorMessage;
				}
		).collect(Collectors.joining(" \n "));
		return new ResponseEntity<>(createResponse(HttpStatus.BAD_REQUEST, message, request), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorDTO> noHandlerFoundException(NoHandlerFoundException exception, WebRequest request) {
		log.info(exception.getMessage(), exception);
		return new ResponseEntity<>(createResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request), HttpStatus.BAD_REQUEST);
	}


	private ErrorDTO createResponse(HttpStatus httpStatus, String message, WebRequest request) {
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setCode(httpStatus.value());
		errorDTO.setType(ERROR);
		errorDTO.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
		errorDTO.setMessage(message);
		log.info(errorDTO.toString());
		return errorDTO;
	}


	@ExceptionHandler({
			ClarityException.class,
			ClarityException.AuthorizationFailed.class,
			ClarityException.BadRequest.class,
			ClarityException.Edit.class,
			ClarityException.FaultIsComplete.class,
			ClarityException.Forbidden.class,
			ClarityException.InputParamNotValid.class,
			ClarityException.ItemNotFound.class,
			ClarityException.NotFound.class,
			ClarityException.Post.class,
			ClarityException.ServerError.class,
			ClarityException.ValidationFailure.class,
			HystrixRuntimeException.class,
			CompletionException.class,
			FeignException.class
	})
	public ResponseEntity<ErrorDTO> exceptionHandler(Exception e, WebRequest request) {
		log.error(e.getMessage(), e);
		String message = "";
		Throwable exception = e.getCause() != null ? e.getCause() : e;

		ResponseStatus httpStatusAnnotation = getExceptionErrorCode(exception);
		HttpStatus httpStatus = httpStatusAnnotation != null ? httpStatusAnnotation.value() : HttpStatus.INTERNAL_SERVER_ERROR;
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setCode(httpStatus.value());

		if (exception instanceof BaseException || exception instanceof HystrixRuntimeException
				|| exception instanceof FeignException) {
			message = exception.getMessage();
			if (message != null) {
				if (message.contains("#")) {
					log.info(exception.getMessage(), exception);
					message = message.split("#")[0] + messageByLocaleComponent.getMessage("out.of.service.err.msg");
				} else if (message.contains(LOAD_BALANCER_ERROR)) {
					log.info(exception.getMessage(), exception);
					message = message.split(":")[2] + messageByLocaleComponent.getMessage("out.of.service.err.msg");
				}

				if (message.toUpperCase().contains(SSO_SERVICE.toUpperCase())) {
					message = messageByLocaleComponent.getMessage("token.expired.err.msg");
					errorDTO.setCode(HttpStatus.UNAUTHORIZED.value());
					httpStatus = HttpStatus.UNAUTHORIZED;
				}
			}
		} else {
			log.info(exception.getMessage(), exception);
			message = messageByLocaleComponent.getMessage("internal.server.server.err.msg");
		}

		errorDTO.setType(ERROR);
		errorDTO.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
		errorDTO.setMessage(message);
		log.info(errorDTO.toString());
		return new ResponseEntity<>(errorDTO, httpStatus);
	}

	private ResponseStatus getExceptionErrorCode(Throwable exception) {
		return AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);
	}

	@Order
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleRuntimeExceptions(Exception exception, WebRequest request) {
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setCode(INTERNAL_SERVER_ERROR.value());
		errorDTO.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
		errorDTO.setMessage(messageByLocaleComponent.getMessage("internal.server.server.err.msg"));
		log.error(exception.getMessage(), exception);
		log.info(errorDTO.toString());
		return new ResponseEntity<>(errorDTO, INTERNAL_SERVER_ERROR);
	}
}
