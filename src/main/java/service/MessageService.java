package service;

import domain.Discussion;
import domain.Message;
import persistence.db.MessageMapper;

import java.sql.SQLException;
import java.time.LocalDateTime;
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

        List<Message> messages = messageMapper.findByDiscussion(discussion);
        messages.forEach((Message m) -> {
            if (m.isCode()) {
                try {
                    m.setMessage(SecurityService.getInstance().decrypt(m.getMessage()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return messages;
    }

    public Message create(Message message) {
        message.setDateExpedition(LocalDateTime.now());

        if (message.isCode()) {
            try {
                message.setMessage(SecurityService.getInstance().encrypt(message.getMessage()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return messageMapper.create(message);
    }
}
