package service;

import domain.Discussion;
import domain.IUser;
import domain.User;
import lombok.Data;
import persistence.db.UserMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserService {
    public static UserService instance = null;

    private SecurityService securityService;
    private UserMapper userMapper;
    private User connectedUser;

    private List<IUser> users;

    private UserService() {
        this.userMapper = UserMapper.getInstance();
        this.securityService = SecurityService.getInstance();
        this.users = new ArrayList<>();
        this.users = this.findAll();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void create(User user) throws Exception {
        user.setPassword(this.securityService.encrypt(user.getPassword()));
        userMapper.create(user);
    }

    public void updateAccount(User user) {
        userMapper.updateAccount(user);
    }

    public void updatePassword(IUser user) {
        try {
            user.setPassword(SecurityService.getInstance().encrypt(user.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        userMapper.updatePassword(user);
    }

    public User findByCredentials(String login, String password) throws Exception {
        this.connectedUser = userMapper.findByCredentials(login, securityService.encrypt(password));
        return this.connectedUser;
    }

    public List<IUser> findAll() {
        return userMapper.findAll();
    }

    public List<IUser> findConnectedUserFriends() {
        List<IUser> friends = userMapper.findFriends(connectedUser);
        friends = friends.stream()
                    .filter((IUser u) -> u.getId() != connectedUser.getId())
                    .collect(Collectors.toList());

        return friends;
    }

    public void delete(IUser user) {
        this.users = this.users.stream()
                .filter((IUser u) -> u.getId() != user.getId())
                .collect(Collectors.toList());
        userMapper.delete(user);
    }

    public List<IUser> findByDiscussion(Discussion discussion) throws SQLException {
        return userMapper.findByDiscussion(discussion);
    }
}
