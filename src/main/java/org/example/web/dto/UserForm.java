package org.example.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserForm {

    @NotEmpty
    private String login;
    @NotEmpty
    @Size(min = 8, max = 10)
    private String password;
    @NotEmpty
    private String repeatPassword;

    public String getRepeatPassword() { return this.repeatPassword; }
    public void setRepeatPassword(String value) { this.repeatPassword = value; }

    public String getLogin() { return this.login; }
    public void setLogin(String value) { this.login = value; }

    public String getPassword() { return this.password; }
    public void setPassword(String value) { this.password = value; }
}
