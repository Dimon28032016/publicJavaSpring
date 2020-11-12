package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Message;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MessageRepository implements ProjectRepository<Message> {

    private final Logger log = Logger.getLogger(MessageRepository.class);
    private final List<Message> repo = new ArrayList<>();

    @Override
    public List<Message> retreiveAll() {
        log.info("retreive all completed");
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Message error) {
        repo.add(error);
        log.info("store error completed: " + error);
    }

    @Override
    public boolean remove(String typeRemove, Object valueRemove) {
        return false;
    }
}
