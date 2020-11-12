package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.FileService;
import org.example.app.services.MessageService;
import org.example.web.dto.FileDownload;
import org.example.web.exceptions.ValidationErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/files")
public class FileDownloadController {

    private final Logger log = Logger.getLogger(FileDownloadController.class);
    private FileService fileService;
    private MessageService messageService;
    private HashMap<String, File> whiteList;

    @PostConstruct
    public void initServerDir(){
        whiteList = new HashMap<>();
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        if(dir.exists()) {
            for(File file : dir.listFiles()) {
                FileDownload fileDownload = new FileDownload();
                fileDownload.setName(file.getName());
                fileDownload.setNameUrl(file.getName().replaceAll("\\.", "%2e")); // Точка);
                fileDownload.setSize(file.length() / 1024);
                /*WhiteList*/
                whiteList.put(file.getName(), file);
                /*WhiteList*/
                fileService.setFile(fileDownload);
            }
        }
    }

    @Autowired
    public FileDownloadController(FileService fileService, MessageService messageService) {

        this.fileService = fileService;
        this.messageService = messageService;
    }

    @GetMapping
    public String getFiles(Model model) {
        model.addAttribute("files", fileService.getFiles());
        model.addAttribute("errorList", messageService.getAllMessage());
        return "download_page";
    }

    private File checkWhitList(String fileName) throws ValidationErrorException {

        File file = whiteList.get(fileName);
        if(file == null)
        {
            throw new ValidationErrorException(500, "File nod found!", "/files");
        }
        return file;
    }

    @GetMapping("/download/{filename}")
    public String downloadFile(
            @PathVariable("filename") String fileName,
            HttpServletResponse response
    ) throws ValidationErrorException {
        log.info("DOWNLOAD FILE = " + fileName);
        String originalFileName = fileName.replaceAll("%2e", "\\.");
        log.info("DOWNLOAD ORIGINAL FILE = " + originalFileName);

        String rootPath = System.getProperty("catalina.home");
        File file = checkWhitList(originalFileName);
        if (file.exists()){
            response.setHeader("Content-disposition", "attachment;filename=" + originalFileName);
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Length", String.valueOf(file.length()));

            try {
                Path pathFile = Paths.get(rootPath + File.separator + "external_uploads" + File.separator + originalFileName);
                Files.copy(pathFile, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException e) {
                log.info("Error writing file to output stream. Filename was '{}'" + fileName, e);
                throw new RuntimeException("IOError writing file to output stream");
            }
        } else {
            throw new ValidationErrorException(404, "File " + originalFileName + " not found", "/files");
        }

        return "redirect:/files";
    }
}
