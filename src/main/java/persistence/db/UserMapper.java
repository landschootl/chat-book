package persistence.db;

import domain.Discussion;
import domain.IUser;
import domain.User;
import domain.enums.ERole;
import persistence.uow.UnitOfWork;
import persistence.vp.UserFactory;
import persistence.vp.VirtualProxyBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant le mapper d'un utilisateur.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
public class UserMapper extends Mapper {
    public static UserMapper instance = null;

    private UserMapper() {
        super();
    }

    public static UserMapper getInstance() {
        if (instance == null) {
            instance = new UserMapper();
        }
        return instance;
    }

    /**
     * Retourne un utilisateur en fonction du login / mot de passe.
     * @param login
     * @param password
     * @return
     * @throws SQLException
     */
    public User findByCredentials(String login, String password) throws SQLException {
        User user = null;
        preparedStatement = db.prepareStatement(this.bundle.getString("select.user.by.credentials"));
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()) {
            user = createUser(rs);
        }
        rs.close();

        return user;
    }

    /**
     * Créé un utilisateur en base de données.
     * @param user
     */
    public void create(User user) {
        try {
            PreparedStatement preparedStatement = db.prepareStatement(this.bundle.getString("create.user"));
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getFirstname());
            preparedStatement.setString(3, user.getLastname());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne la liste des utilisateurs
     * @return
     */
    public List<IUser> findAll() {
        List<IUser> users = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(this.bundle.getString("select.users.all"));
            while(rs.next()) {
                users.add(new VirtualProxyBuilder<>(IUser.class, new UserFactory(rs.getString("id"))).getProxy());
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Retourne la liste des utilisateurs d'une discussion.
     * @param discussion
     * @return
     * @throws SQLException
     */
    public List<IUser> findByDiscussion(Discussion discussion) throws SQLException {
        List<IUser> users = new ArrayList<>();
        preparedStatement = db.prepareStatement(this.bundle.getString("select.users.by.discussion"));
        preparedStatement.setInt(1, discussion.getId());
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()) {
            users.add(new VirtualProxyBuilder<>(IUser.class, new UserFactory(rs.getString("id"))).getProxy());
        }
        rs.close();

        return users;
    }

    /**
     * Retourne un utilisateur par son identifiant.
     * @param identifiant
     * @return
     * @throws SQLException
     * @throws NoDataFoundException
     */
    public IUser findByIdentifiant(String identifiant) throws SQLException, NoDataFoundException {
        IUser user;
        preparedStatement = db.prepareStatement(this.bundle.getString("select.user.by.identifiant"));
        preparedStatement.setString(1, identifiant);
        ResultSet rs = preparedStatement.executeQuery();

        if(rs.next()) {
            user = createUser(rs);
        } else {
            throw new NoDataFoundException("Utilisateur introuvable");
        }
        rs.close();

        return user;
    }

    /**
     * Retourne les amis d'un utilisateur.
     * @param user
     * @return
     */
    public List<IUser> findFriends(IUser user) {
        List<IUser> friends = new ArrayList<>();
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("select.user.friends"));
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, user.getId());
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                friends.add(new VirtualProxyBuilder<>(IUser.class, new UserFactory(rs.getString("id"))).getProxy());
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    /**
     * Mise à jour d'un utilisateur avec son rôle.
     * @param user
     */
    public void update(User user) {
        try {
            PreparedStatement preparedStatement = db.prepareStatement(this.bundle.getString("update.user.by.identifiant"));
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getFirstname());
            preparedStatement.setString(3, user.getLastname());
            preparedStatement.setString(4, user.getRole().toString());
            preparedStatement.setInt(5, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mise à jour d'un utilisateur sans rôle.
     * @param user
     */
    public void updateAccount(User user) {
        try {
            PreparedStatement preparedStatement = db.prepareStatement(this.bundle.getString("update.user.account"));
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getFirstname());
            preparedStatement.setString(3, user.getLastname());
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mise à jour du mot de passe d'un utilisateur.
     * @param user
     */
    public void updatePassword(IUser user) {
        try {
            PreparedStatement preparedStatement = db.prepareStatement(this.bundle.getString("update.user.password"));
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Supprime d'un utilisateur.
     * @param user
     */
    public void delete(IUser user) {
        try {
            PreparedStatement preparedStatement = db.prepareStatement(this.bundle.getString("delete.user.by.identifiant"));
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Créé un
     * @param rs
     * @return
     * @throws SQLException
     */
    private User createUser(ResultSet rs) throws SQLException {
        User user;
        user = User.builder()
                .id(rs.getInt("id"))
                .login(rs.getString("login"))
                .firstname(rs.getString("firstname"))
                .lastname(rs.getString("lastname"))
                .role(ERole.valueOf(rs.getString("role")))
                .password(rs.getString("password"))
                .obs(new ArrayList<>())
                .build();
        user.addObserver(UnitOfWork.getInstance());
        return user;
    }
}
