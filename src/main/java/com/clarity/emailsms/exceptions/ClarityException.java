package com.clarity.emailsms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ClarityException extends BaseException {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class NotFound extends BaseException {
        public NotFound(String msg, Object... args) {
            super(msg, args);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class ValidationFailure extends BaseException {
        public ValidationFailure(String msg, Object... args) {
            super(msg, args);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class BadRequest extends BaseException {
        public BadRequest(String msg, Object... args) {
            super(msg, args);
        }
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class AuthorizationFailed extends BaseException {
        public AuthorizationFailed(String msg, Object... args) {
            super(msg, args);
        }
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class Forbidden extends BaseException {
        public Forbidden(String msg, Object... args) {
            super(msg, args);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class Post extends BaseException {
        public Post(String msg, Object... args) {
            super(msg, args);
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public static class FaultIsComplete extends BaseException {
        public FaultIsComplete(String msg, Object... args) {
            super(msg, args);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class InputParamNotValid extends BaseException {
        public InputParamNotValid(String msg, Object... args) {
            super(msg, args);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ItemNotFound extends BaseException {
        public ItemNotFound(String msg, Object... args) {
            super(msg, args);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class Edit extends BaseException {
        public Edit(String msg, Object... args) {
            super(msg, args);
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public static class ServerError extends BaseException {
        public ServerError(String msg, Object... args) {
            super(msg, args);
        }
    }
}
