package service;

import domain.Discussion;
import domain.Message;
import persistence.db.MessageMapper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by landschoot on 11/12/16.
 */
public class MessageService {
    public static MessageService instance = null;
    private MessageMapper messageMapper;

    private MessageService(){
        messageMapper = MessageMapper.getInstance();
    }

    public static MessageService getInstance() {
        if (instance == null) {
            instance = new MessageService();
        }
        return instance;
    }

    public List<Message> findByDiscussion(Discussion discussion) throws SQLException {
        return messageMapper.findByDiscussion(discussion);
    }

    public Message create(Message message){
        return messageMapper.create(message);
    }
}
