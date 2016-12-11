package persistence.db;

import domain.Discussion;
import domain.IUser;
import domain.Message;
import persistence.vp.UserFactory;
import persistence.vp.VirtualProxyBuilder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public Message create(Message message) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("create.message"), PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getIdConnection());
            preparedStatement.setInt(2, message.getUser().getId());
            preparedStatement.setString(3, message.getMessage());
            preparedStatement.setBoolean(4, message.isAccused());
            preparedStatement.setBoolean(5, message.isPriority());
            preparedStatement.setDate(6, message.getExpiration() == null ? null : Date.valueOf(message.getExpiration()));
            preparedStatement.setBoolean(7, message.isCode());
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
