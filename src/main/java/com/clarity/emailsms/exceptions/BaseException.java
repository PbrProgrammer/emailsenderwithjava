package com.clarity.emailsms.exceptions;

public class BaseException extends RuntimeException {

    //Each exception message will be held here
    private String message;

    public BaseException() {
        super();
    }

    public BaseException(String msg, Object... args) {
        this.message = format(msg, args);
    }

    //Message can be retrieved using this accessor method
    @Override
    public String getMessage() {
        return message;
    }

    private static String format(String msg, Object... args) {
        msg = msg.replace("{}", "%S");
        msg = msg.replace("[]", "%S");
        return String.format(msg, args);
    }
}
