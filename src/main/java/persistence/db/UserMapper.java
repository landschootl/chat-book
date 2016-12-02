package persistence.db;

import domain.User;

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
                    .role(rs.getString(5))
                    .build();
        }
        rs.close();

        return user;
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        ResultSet rs = statement.executeQuery(this.bundle.getString("select.all.users"));

        while(rs.next()) {
            users.add(User.builder()
                    .id(rs.getInt(1))
                    .login(rs.getString(2))
                    .firstname(rs.getString(3))
                    .lastname(rs.getString(4))
                    .role(rs.getString(5))
                    .build());
        }
        rs.close();

        return users;
    }
}
