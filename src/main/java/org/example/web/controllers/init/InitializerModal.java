package org.example.web.controllers.init;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.app.services.MessageService;
import org.example.web.controllers.BookShelfController;
import org.example.web.exceptions.ValidationErrorException;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.HashMap;

public class InitializerModal {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private HashMap<String, String> modelKey;
    //private BookService bookService;
    //private MessageService messageService;

    /*private Object refresh(String key) {
        switch (key) {
            case "bookList":
                return bookService.getAllBooks();
            case "errorList":
                return messageService.getAllMessage();
        }
        return null;
    }*/

    private Object initClass(String strClass) {
        logger.info("init " + strClass);
        Class<?> clazz = null;
        Object instance = null;
        try {
            clazz = Class.forName(strClass);
            instance = clazz.newInstance();
        } catch (InstantiationException e) {
            logger.error(e);;
        } catch (IllegalAccessException e) {
            logger.error(e);
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
        return instance;
    }

    protected void setConfigure(
            HashMap<String, String> modelKey/*,
            BookService bookService,
            MessageService messageService*/
    ) {
        this.modelKey = modelKey;
       // this.messageService = messageService;
       // this.bookService = bookService;
    }

    protected Model initModel(Model model, String[] filterModal, HashMap<String, Object> refresh) throws ValidationErrorException {
        if(modelKey != null/* && messageService != null && bookService != null*/) {
            HashMap<String, String> requestMap = new HashMap<String, String>(modelKey);
            if (filterModal != null) {
                for (String filter : filterModal) {
                    requestMap.remove(filter);
                }
            }
            for (String key : requestMap.keySet()) {
                if (refresh != null && Arrays.asList(refresh.keySet().toArray()).indexOf(key) != -1) {
                    model.addAttribute(key, refresh.get(key));
                } else {
                    model.addAttribute(key, initClass(requestMap.get(key)));
                }
            }
        } else {
            throw new ValidationErrorException(500, "not install configure model", "/books/shelf");
        }
        return model;
    }

}
