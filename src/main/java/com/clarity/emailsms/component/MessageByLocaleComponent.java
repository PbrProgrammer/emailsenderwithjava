package com.clarity.emailsms.component;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Configuration
public class MessageByLocaleComponent {

    private final MessageSource messageSource;
    private final Locale locale;

    public MessageByLocaleComponent(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.locale = LocaleContextHolder.getLocale();
    }

    public String getMessage(String s) {
        return messageSource.getMessage(s, null, locale);
    }

    public String getMessage(String s, Object[] objects, String s1) {
        return messageSource.getMessage(s, objects, s1, locale);
    }

    public String getMessage(String s, Object[] objects) {
        return messageSource.getMessage(s, objects, locale);
    }


    public String getMessage(MessageSourceResolvable messageSourceResolvable) {
        return messageSource.getMessage(messageSourceResolvable, locale);
    }
}
