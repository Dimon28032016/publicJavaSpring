package org.example.web.controllers;

import org.example.app.services.MessageService;
import org.example.web.exceptions.ValidationErrorException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationErrorController {

    private final MessageService messageService;

    public ValidationErrorController(MessageService messageService) {
        this.messageService = messageService;
    }

    @ExceptionHandler(ValidationErrorException.class)
    public String validErrorHandler(ValidationErrorException exception) {
        this.messageService.saveError(exception.getMessageDto());
        return "redirect:" + exception.getRedirect();
    }
}
