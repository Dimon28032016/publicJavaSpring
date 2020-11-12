package org.example.app.services;

import org.example.web.dto.FileDownload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private FileRepository repository;

    @Autowired
    public FileService(FileRepository fileRepository){
        this.repository = fileRepository;
    }

    public List<FileDownload> getFiles() { return  repository.retreiveAll(); }

    public void setFile(FileDownload file) { repository.store(file);}
}
