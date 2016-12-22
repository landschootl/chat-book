package service;

import domain.Discussion;
import domain.Message;
import persistence.db.MessageMapper;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe représentant le service d'un message.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
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

    /**
     * Retourne la liste des messages d'une discussion.
     * @param discussion
     * @return
     * @throws SQLException
     */
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

    /**
     * Mise à jour de l'accusé de réception.
     * @param message
     */
    public void updateDateAccused(Message message) {
        messageMapper.updateDateAccused(message);
    }

    /**
     * Créé un message en base de données.
     * @param message
     * @return
     */
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
