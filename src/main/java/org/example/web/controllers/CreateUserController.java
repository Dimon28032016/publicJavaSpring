package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.MessageService;
import org.example.app.services.UserService;
import org.example.web.controllers.init.InitializerModal;
import org.example.web.dto.UserForm;
import org.example.web.exceptions.ValidationErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/user")
public class CreateUserController extends InitializerModal {

    private Logger log = Logger.getLogger(CreateUserController.class);
    private UserService userService;
    private MessageService messageService;
    private HashMap<String, String> modelKey;

    @Autowired
    public CreateUserController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @PostConstruct
    public void initModelKey() {
        modelKey = new HashMap<>();
        modelKey.put("userForm", UserForm.class.getName());
        modelKey.put("userList", "");
        modelKey.put("errorList", "");
    }

    @GetMapping
    public String users(Model model) {
        log.info("GET /user returns create_user_page.html");
        setConfigure(modelKey);
        HashMap<String, Object> refresh = new HashMap<>();
        refresh.put("userList", userService.getAllUsers());
        refresh.put("errorList", messageService.getAllMessage());
        try {
            model = initModel(model, null, refresh);
        } catch (ValidationErrorException exception) {
            log.error(exception);
        }
        return "create_user_page";
    }

    @PostMapping("/create")
    public String createUser(
            @Valid UserForm user,
            BindingResult bindingResult,
            Model model
    ) throws ValidationErrorException {
        if(bindingResult.hasErrors()) {
            String[] filter = new String[] {"userForm"};
            HashMap<String, Object> refresh = new HashMap<>();
            refresh.put("userList", userService.getAllUsers());
            refresh.put("errorList", messageService.getAllMessage());
            model = initModel(model, filter, refresh);
            return "create_user_page";
        }

        userService.saveUser(user);
        log.info("user creat completed: " + user);
        return "redirect:/user";
    }
}
