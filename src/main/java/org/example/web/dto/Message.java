package org.example.web.dto;

public class Message {
    private Integer code;
    private String message;

    public Integer getCode(){ return this.code; }
    public void setCode(Integer value) { this.code = value;}

    public String getMessage() { return this.message; }
    public void setMessage(String value) { this.message = value;}
}
