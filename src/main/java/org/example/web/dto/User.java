package org.example.web.dto;

public class User {

    private Integer id;
    private String login;
    private String password;

    public Integer getId() { return this.id; }
    public void setId(Integer value) { this.id = value; }

    public String getLogin() { return this.login; }
    public void setLogin(String value) { this.login = value; }

    public String getPassword() { return this.password; }
    public void setPassword(String value) { this.password = value; }
}
