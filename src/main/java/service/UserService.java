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

/**
 * Classe représentant le service d'utilisateur.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
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

    /**
     * Créé un utilisateur en base de données.
     * @param user
     */
    public void create(User user) throws Exception {
        user.setPassword(this.securityService.encrypt(user.getPassword()));
        userMapper.create(user);
    }

    /**
     * Mise à jour d'un utilisateur sans rôle.
     * @param user
     */
    public void updateAccount(User user) {
        userMapper.updateAccount(user);
    }

    /**
     * Mise à jour du mot de passe d'un utilisateur.
     * @param user
     */
    public void updatePassword(IUser user) {
        try {
            user.setPassword(SecurityService.getInstance().encrypt(user.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        userMapper.updatePassword(user);
    }

    /**
     * Retourne un utilisateur en fonction du login / mot de passe.
     * @param login
     * @param password
     * @return
     * @throws SQLException
     */
    public User findByCredentials(String login, String password) throws Exception {
        this.connectedUser = userMapper.findByCredentials(login, securityService.encrypt(password));
        return this.connectedUser;
    }

    /**
     * Retourne la liste des utilisateurs
     * @return
     */
    public List<IUser> findAll() {
        return userMapper.findAll();
    }

    /**
     * Retourne les amis de l'utilisateur connecté.
     * @param user
     * @return
     */
    public List<IUser> findConnectedUserFriends() {
        List<IUser> friends = userMapper.findFriends(connectedUser);
        friends = friends.stream()
                    .filter((IUser u) -> u.getId() != connectedUser.getId())
                    .collect(Collectors.toList());

        return friends;
    }

    /**
     * Supprime d'un utilisateur.
     * @param user
     */
    public void delete(IUser user) {
        this.users = this.users.stream()
                .filter((IUser u) -> u.getId() != user.getId())
                .collect(Collectors.toList());
        userMapper.delete(user);
    }

    /**
     * Retourne la liste des utilisateurs d'une discussion.
     * @param discussion
     * @return
     * @throws SQLException
     */
    public List<IUser> findByDiscussion(Discussion discussion) throws SQLException {
        return userMapper.findByDiscussion(discussion);
    }
}
