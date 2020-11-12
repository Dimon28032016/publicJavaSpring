package org.example.app.services;

import org.example.web.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final ProjectRepository<Message> messageRepo;

    @Autowired
    public MessageService(ProjectRepository<Message> messageRepo) { this.messageRepo = messageRepo; }

    public List<Message> getAllMessage() {
        return this.messageRepo.retreiveAll();
    }

    public void saveError(Message error) {
        this.messageRepo.store(error);
    }
}
