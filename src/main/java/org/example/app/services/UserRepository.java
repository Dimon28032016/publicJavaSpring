package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository implements ProjectRepository<User> {

    private final Logger log = Logger.getLogger(UserRepository.class);
    private final List<User> repo = new ArrayList<>();

    public UserRepository() {
        User admin = new User();
        admin.setLogin("root");
        admin.setPassword("123");
        store(admin);
    }

    @Override
    public List<User> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(User user) {
        user.setId(user.hashCode());
        repo.add(user);
        log.info("store user completed: " + user);
    }

    @Override
    public boolean remove(String typeRemove, Object valueRemove) {
        for(User user : retreiveAll()) {
            if(user.getId().equals(Integer.getInteger(valueRemove.toString()))) {
                log.info("remove user completed: " + user);
                repo.remove(user);
                return true;
            }
        }
        return false;
    }
}
