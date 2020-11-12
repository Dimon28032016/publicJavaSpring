package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.app.services.FileService;
import org.example.app.services.MessageService;
import org.example.web.controllers.init.InitializerModal;
import org.example.web.dto.*;
import org.example.web.exceptions.ValidationErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/books")
public class BookShelfController extends InitializerModal {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;
    private MessageService errorService;
    private FileService fileService;
    private HashMap<String, String> modelKey = new HashMap<>();

    @PostConstruct
    public void initHasMap() {

        modelKey.put("book", Book.class.getName());
        modelKey.put("bookList", "");
        modelKey.put("errorList", "");
        modelKey.put("filterForm", FilterForm.class.getName());
        modelKey.put("bookRemoveForm", BookRemoveForm.class.getName());
        modelKey.put("uploadFileForm", UploadFileForm.class.getName());
    }

    @Autowired
    public BookShelfController(BookService bookService, MessageService errorService, FileService fileService) {
        this.errorService = errorService;
        this.bookService = bookService;
        this.fileService = fileService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf " + Book.class);
        HashMap<String, Object> refresh = new HashMap<>();
        refresh.put("bookList", bookService.getAllBooks());
        refresh.put("errorList", errorService.getAllMessage());
        setConfigure(modelKey);
        try {
            model = initModel(model, null, refresh);
        } catch (ValidationErrorException exception) {
            logger.error(exception);
        }
        logger.info("Model result " + model);
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(
            @Valid Book book,
            BindingResult bindingResult,
            Model model
    ) throws ValidationErrorException {

        if(
                bindingResult.hasFieldErrors("title") &&
                bindingResult.hasFieldErrors("size") &&
                bindingResult.hasFieldErrors("author")
        ) {
            throw new ValidationErrorException(204, "No Content " + book, "/books/shelf");
        }
        else if(bindingResult.hasErrors()) {
            String[] filter = new String[]{"book"};
            HashMap<String, Object> refresh = new HashMap<>();
            refresh.put("bookList", bookService.getAllBooks());
            refresh.put("errorList", errorService.getAllMessage());
            model = initModel(model, filter, refresh);
            return "book_shelf";
        }

        bookService.saveBook(book);
        logger.info("current repository size: " + bookService.getAllBooks().size());
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove")
    public String removeBook(
            @Valid BookRemoveForm bookRemoveForm,
            BindingResult bindingResult,
            Model model
    ) throws ValidationErrorException {

        if(bindingResult.hasErrors() || bookRemoveForm.getValue().equals("")) {
            String[] filter = new String[]{"bookRemoveForm"};
            HashMap<String, Object> refresh = new HashMap<>();
            refresh.put("bookList", bookService.getAllBooks());
            refresh.put("errorList", errorService.getAllMessage());
            model = initModel(model, filter, refresh);
            return "book_shelf";
        }

        try {
            if (!bookService.remove(bookRemoveForm.getType(), bookRemoveForm.getValue())) {
                throw new ValidationErrorException(
                        404,
                        "book: { type:" + bookRemoveForm.getType() + ", value:" + bookRemoveForm.getValue() + " } not found!",
                        "/books/shelf"
                );
            }
        } catch (NumberFormatException exception) {
            throw new ValidationErrorException(404, "NumberFormatException: " + exception.getMessage(), "/books/shelf");
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/filterBook")
    public String filterBook(
            @Valid FilterForm filterForm,
            BindingResult bindingResult,
            Model model
    ) throws ValidationErrorException {
        if(bindingResult.hasErrors()) {
            String[] filter = new String[]{"filterForm"};
            HashMap<String, Object> refresh = new HashMap<>();
            refresh.put("bookList", bookService.getAllBooks());
            refresh.put("errorList", errorService.getAllMessage());
            model = initModel(model, filter, refresh);
            return "book_shelf";
        }

        bookService.initFilter(filterForm);
        return "redirect:/books/shelf";
    }

    @PostMapping("/resetFilter")
    public String resetFilter() {
        bookService.initFilter(null);
        return "redirect:/books/shelf";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(
            @Valid UploadFileForm uploadFileForm,
            BindingResult bindingResult,
            Model model
    ) throws ValidationErrorException, IOException {

        if(bindingResult.hasErrors()) {
            String[] filter = new String[] {"uploadFileForm"};
            HashMap<String, Object> refresh = new HashMap<>();
            refresh.put("bookList", bookService.getAllBooks());
            refresh.put("errorList", errorService.getAllMessage());
            model = initModel(model, filter, refresh);
            return "book_shelf";
        }

        String nameFile = uploadFileForm.getFile().getOriginalFilename();
        if(nameFile.indexOf("&#") != -1 ||
           nameFile.indexOf("!") != -1 ||
           nameFile.indexOf("@") != -1 ||
           nameFile.indexOf("#") != -1 ||
           nameFile.indexOf("$") != -1 ||
           nameFile.indexOf("%") != -1 ||
           nameFile.indexOf("&") != -1 ||
           nameFile.indexOf(":") != -1 ||
           nameFile.indexOf(";") != -1
        ) {
            throw new ValidationErrorException(400, "the name of the uploaded file must consist only of Latin characters", "/books/shelf");
        }
        logger.info("UPLOAD FILE = " + nameFile);
        byte[] bytesFile = uploadFileForm.getFile().getBytes();

        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        if(!dir.exists()) {
            dir.mkdir();
        }

        File serverFile = new File(dir.getAbsolutePath() + File.separator + nameFile);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(serverFile));
        bufferedOutputStream.write(bytesFile);
        bufferedOutputStream.close();

        logger.info("BufferedOutputStream closed");
        FileDownload fileDownload = new FileDownload();
        fileDownload.setName(nameFile);
        fileDownload.setSize(serverFile.length() / 1024);
        fileDownload.setNameUrl(nameFile.replaceAll("\\.", "%2e")); // Точка);
        fileService.setFile(fileDownload);
        return "redirect:/books/shelf";
    }

}
