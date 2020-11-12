package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.FileDownload;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FileRepository implements ProjectRepository<FileDownload> {

    private final Logger log = Logger.getLogger(FileRepository.class);
    private List<FileDownload> repo = new ArrayList<>();

    @Override
    public List<FileDownload> retreiveAll() {
        return repo;
    }

    @Override
    public void store(FileDownload file) {
        repo.add(file);
        log.info("Add file in repo");
    }

    @Override
    public boolean remove(String typeRemove, Object valueRemove) {
        return false;
    }

}
