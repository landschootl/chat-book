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

    private User createUser(ResultSet rs) throws SQLException {
        User user;
        user = User.builder()
                .id(rs.getInt(1))
                .login(rs.getString(2))
                .firstname(rs.getString(3))
                .lastname(rs.getString(4))
                .role(ERole.valueOf(rs.getString(5)))
                .obs(new ArrayList<>())
                .build();
        user.addObserver(UnitOfWork.getInstance());
        return user;
    }

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

    public List<IUser> findAll() throws SQLException {
        List<IUser> users = new ArrayList<>();
        ResultSet rs = statement.executeQuery(this.bundle.getString("select.all.users"));

        while(rs.next()) {
            users.add(new VirtualProxyBuilder<>(IUser.class, new UserFactory(rs.getString("id"))).getProxy());
        }
        rs.close();

        return users;
    }

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

    public void update(User user) {
        try {
            PreparedStatement preparedStatement = db.prepareStatement(this.bundle.getString("update.user.by.identifiant"));
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getRole().toString());
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public void delete(IUser user) {
        try {
            PreparedStatement preparedStatement = db.prepareStatement(this.bundle.getString("delete.user.by.identifiant"));
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
