package persistence.db;

import domain.Discussion;
import domain.IUser;
import domain.Message;
import persistence.vp.UserFactory;
import persistence.vp.VirtualProxyBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by landschoot on 11/12/16.
 */
public class MessageMapper extends Mapper {
    public static MessageMapper instance = null;

    public MessageMapper(){
        super();
    }

    public static MessageMapper getInstance() {
        if (instance == null) {
            instance = new MessageMapper();
        }
        return instance;
    }

    private Message createMessage(ResultSet rs) throws SQLException {
        Message message = Message.builder()
                .id(rs.getInt("id"))
                .idConnection(rs.getInt("id_connection"))
                .user(new VirtualProxyBuilder<>(IUser.class, new UserFactory(rs.getString("id_user"))).getProxy())
                .message(rs.getString("message"))
                .dateExpedition(rs.getDate("date_expedition") == null ? null : rs.getTimestamp("date_expedition").toLocalDateTime())
                .dateAccused(rs.getDate("date_accused") == null ? null : rs.getTimestamp("date_accused").toLocalDateTime())
                .accused(rs.getBoolean("accused"))
                .priority(rs.getBoolean("priority"))
                .expiration(rs.getDate("expiration") == null ? null : rs.getDate("expiration").toLocalDate())
                .code(rs.getBoolean("code"))
                .build();
        return message;
    }

    public List<Message> findByDiscussion(Discussion discussion) throws SQLException {
        List<Message> messages = new ArrayList<>();
        preparedStatement = db.prepareStatement(this.bundle.getString("select.message.by.discussion"));
        preparedStatement.setInt(1, discussion.getId());
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()) {
            messages.add(createMessage(rs));
        }
        rs.close();
        return messages;
    }

    public void updateDateAccused(Message message) {
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("update.message.date.accused"));
            preparedStatement.setTimestamp(1, message.getDateAccused() == null ? null : Timestamp.valueOf(message.getDateAccused()));
            preparedStatement.setInt(2, message.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Message create(Message message) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("create.message"), PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getIdConnection());
            preparedStatement.setInt(2, message.getUser().getId());
            preparedStatement.setString(3, message.getMessage());
            preparedStatement.setTimestamp(4, message.getDateExpedition() == null ? null : Timestamp.valueOf(message.getDateExpedition()));
            preparedStatement.setTimestamp(5, message.getDateAccused() == null ? null : Timestamp.valueOf(message.getDateAccused()));
            preparedStatement.setBoolean(6, message.isAccused());
            preparedStatement.setBoolean(7, message.isPriority());
            preparedStatement.setDate(8, message.getExpiration() == null ? null : Date.valueOf(message.getExpiration()));
            preparedStatement.setBoolean(9, message.isCode());
            preparedStatement.executeUpdate();
            ResultSet resultId = preparedStatement.getGeneratedKeys();
            resultId.next();
            message.setId(resultId.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }
}
