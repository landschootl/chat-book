package service;

import persistence.db.MessageMapper;

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

}
