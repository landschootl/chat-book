package service;

import domain.User;
import lombok.Data;
import persistence.db.UserMapper;

import java.sql.SQLException;
import java.util.List;

@Data
public class UserService {
    public static UserService instance = null;

    private SecurityService securityService;
    private UserMapper userMapper;
    private User connectedUser;

    private UserService() {
        this.userMapper = UserMapper.getInstance();
        this.securityService = SecurityService.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User findByCredentials(String login, String password) throws Exception {
        this.connectedUser = userMapper.findByCredentials(login, securityService.encrypt(password));
        return this.connectedUser;
    }

    public List<User> findAll() throws SQLException {
        return userMapper.findAll();
    }
    
}
