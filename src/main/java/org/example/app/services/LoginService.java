package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.LoginForm;
import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private Logger logger = Logger.getLogger(LoginService.class);
    @Autowired
    private UserService userService;

    public boolean authenticate(LoginForm loginFrom) {
        logger.info("try auth with user-form: " + loginFrom);
        boolean inExit = false;
        for(User user : userService.getAllUsers()) {
            if(loginFrom.getUsername().equals(user.getLogin())
                    && loginFrom.getPassword().equals(user.getPassword())) {
                inExit = true;
            }
        }
        return inExit;
    }
}
