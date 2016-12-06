package persistence.db;

import com.sun.corba.se.impl.orbutil.graph.NodeData;
import domain.IUser;
import domain.User;
import domain.enums.Role;
import persistence.uow.UnitOfWork;

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

    public User findByCredentials(String login, String password) throws SQLException {
        User user = null;
        preparedStatement = db.prepareStatement(this.bundle.getString("select.user.by.credentials"));
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()) {
            user = User.builder()
                    .id(rs.getInt(1))
                    .login(rs.getString(2))
                    .firstname(rs.getString(3))
                    .lastname(rs.getString(4))
                    .role(Role.valueOf(rs.getString(5)))
                    .obs(new ArrayList<>())
                    .build();
            user.add(UnitOfWork.getInstance());
        }
        rs.close();

        return user;
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        ResultSet rs = statement.executeQuery(this.bundle.getString("select.all.users"));

        while(rs.next()) {
            User user = User.builder()
                    .id(rs.getInt(1))
                    .login(rs.getString(2))
                    .firstname(rs.getString(3))
                    .lastname(rs.getString(4))
                    .role(Role.valueOf(rs.getString(5)))
                    .obs(new ArrayList<>())
                    .build();
            user.add(UnitOfWork.getInstance());

            users.add(user);
        }
        rs.close();

        return users;
    }

    public IUser findByIdentifiant(String identifiant) throws SQLException, NoDataFoundException {
        IUser user = null;
        preparedStatement = db.prepareStatement(this.bundle.getString("select.user.by.identifiant"));
        preparedStatement.setString(1, identifiant);
        ResultSet rs = preparedStatement.executeQuery();

        if(rs.next()) {
            user = (User) User.builder()
                    .id(rs.getInt(1))
                    .login(rs.getString(2))
                    .firstname(rs.getString(3))
                    .lastname(rs.getString(4))
                    .role(Role.valueOf(rs.getString(5)))
                    .obs(new ArrayList<>())
                    .build();
            user.add(UnitOfWork.getInstance());
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

    public void delete(User user) {
        try {
            PreparedStatement preparedStatement = db.prepareStatement(this.bundle.getString("delete.user.by.identifiant"));
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
