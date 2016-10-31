package service;

import domain.User;
import persistence.UserMapper;

import java.sql.SQLException;

public class UserService {
    public static UserService instance = null;

    private UserMapper userMapper;

    private UserService() {
        this.userMapper = UserMapper.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User findByCredentials(String login, String password) throws SQLException {
        return userMapper.findByCredentials(login, password);
    }
}
