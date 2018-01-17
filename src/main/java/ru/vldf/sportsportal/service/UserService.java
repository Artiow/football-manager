package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.dao.impl.UserDAO;
import ru.vldf.sportsportal.model.UserEntity;

import java.util.List;

@Transactional
@Service("userService")
public class UserService {

    public UserService() {

    }

    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<UserEntity> listUsers() {
        return userDAO.list();
    }
}