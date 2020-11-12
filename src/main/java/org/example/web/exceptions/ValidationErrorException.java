package org.example.web.exceptions;

import org.example.web.dto.Message;


public class ValidationErrorException extends Exception {

    private Message messageDTO;
    private String redirect;

    public ValidationErrorException(Integer codeError, String message, String redirect) {
        this.messageDTO = new Message();
        this.messageDTO.setCode(codeError);
        this.messageDTO.setMessage(message);
        this.redirect = redirect;
    }

    public String getRedirect() {
        return this.redirect;
    }

    public Message getMessageDto() {
        return this.messageDTO;
    }
}
