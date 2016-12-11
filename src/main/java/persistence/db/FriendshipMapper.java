package persistence.db;

import domain.Friendship;
import domain.IUser;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipMapper extends Mapper {
    public static FriendshipMapper instance = null;

    private FriendshipMapper() {
        super();
    }

    public static FriendshipMapper getInstance() {
        if (instance == null) {
            instance = new FriendshipMapper();
        }
        return instance;
    }

    public void create(Friendship friendship) {
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("create.friendship"));
            preparedStatement.setInt(1, friendship.getUser1().getId());
            preparedStatement.setInt(2, friendship.getUser2().getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Friendship find(IUser user1, IUser user2) {
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("select.friendship"));
            preparedStatement.setInt(1, user1.getId());
            preparedStatement.setInt(2, user2.getId());
            preparedStatement.setInt(3, user2.getId());
            preparedStatement.setInt(4, user1.getId());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return Friendship.builder()
                        .user1(user1)
                        .user2(user2)
                        .confirmed(rs.getBoolean("confirmed")).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Friendship friendship) {
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("delete.friendship"));
            preparedStatement.setInt(1, friendship.getUser1().getId());
            preparedStatement.setInt(2, friendship.getUser2().getId());
            preparedStatement.setInt(3, friendship.getUser2().getId());
            preparedStatement.setInt(4, friendship.getUser1().getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
