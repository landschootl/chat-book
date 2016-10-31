package service;

import domain.User;
import lombok.Data;
import persistence.UserMapper;

import java.sql.SQLException;

@Data
public class UserService {
    public static UserService instance = null;

    private UserMapper userMapper;
    private User connectedUser;

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
        this.connectedUser = userMapper.findByCredentials(login, password);
        return this.connectedUser;
    }
}
