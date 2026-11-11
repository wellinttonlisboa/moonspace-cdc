package br.com.moonspace.core.architecture.basic.ddd.exceptions;

import br.com.moonspace.core.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(String message, Notification notification) {
        super(message, notification.getErrors());
    }

}