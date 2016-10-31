package persistence;

import domain.User;
import net.rakugakibox.util.YamlResourceBundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserMapper {
    public static UserMapper instance = null;

    protected Connection db;
    protected ResourceBundle bundle;

    private UserMapper() {
        this.db = SingletonDB.getInstance().getDb();
        this.bundle = ResourceBundle.getBundle("chatbook", YamlResourceBundle.Control.INSTANCE);
    }

    public static UserMapper getInstance() {
        if (instance == null) {
            instance = new UserMapper();
        }
        return instance;
    }

    public User findByCredentials(String login, String password) throws SQLException {
        User user = null;
        PreparedStatement preparedStatement;
        ResultSet rs;

        preparedStatement = db.prepareStatement(this.bundle.getString("select.user.by.credentials"));
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        rs = preparedStatement.executeQuery();

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
}
