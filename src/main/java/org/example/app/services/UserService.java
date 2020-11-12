package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Message;
import org.example.web.dto.User;
import org.example.web.dto.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final Logger log = Logger.getLogger(UserService.class);
    private final ProjectRepository<User> userRepo;
    private final MessageService messageService;

    @Autowired
    private UserService(ProjectRepository<User> userRepo, MessageService messageService) {
        this.userRepo = userRepo;
        this.messageService = messageService;
    }

    public List<User> getAllUsers() {
        return userRepo.retreiveAll();
    }

    public void saveUser(UserForm userFrom) {
        Message message = new Message();
        if(checkPassword(userFrom)){
            if(checkLogin(userFrom)) {
                if(userFrom.getLogin().trim().length() != 0 && userFrom.getPassword().trim().length() != 0) {
                    User user = new User();
                    user.setPassword(userFrom.getPassword());
                    user.setLogin(userFrom.getLogin());
                    userRepo.store(user);
                } else {
                    message.setCode(400);
                    message.setMessage("username or password is empty");
                    messageService.saveError(message);
                }
            } else {
                message.setCode(400);
                message.setMessage("this username already exists");
                messageService.saveError(message);
            }
        } else {
            message.setCode(400);
            message.setMessage("password didnt match");
            messageService.saveError(message);
        }
    }

    public boolean removeUserById(Integer userIdToRemove) {
        return userRepo.remove("", userIdToRemove);
    }

    private boolean checkPassword(UserForm user) {
        if(user.getPassword().equals(user.getRepeatPassword())) {
            return  true;
        }
        return  false;
    }

    private boolean checkLogin(UserForm userForm) {
        boolean existLogin = true;
        for(User user : userRepo.retreiveAll()) {
            if(user.getLogin().equals(userForm.getLogin())) {
                existLogin = false;
                break;
            }
        }
        return  existLogin;
    }
}
