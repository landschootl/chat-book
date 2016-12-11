package persistence.db;

import domain.Discussion;
import domain.Message;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by landschoot on 11/12/16.
 */
public class MessageMaper extends Mapper {
    public static MessageMaper instance = null;

    public MessageMaper(){
        super();
    }

    public static MessageMaper getInstance() {
        if (instance == null) {
            instance = new MessageMaper();
        }
        return instance;
    }

    private Message createMessage(ResultSet rs) throws SQLException {
        Message message = Message.builder()
                .id(rs.getInt("id"))
                .id_connection(rs.getInt("id_connection"))
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
            preparedStatement.setInt(1, message.getId_connection());
            preparedStatement.setInt(2, message.getId_user());
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
