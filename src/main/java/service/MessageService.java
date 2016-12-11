package service;

import persistence.db.MessageMaper;

/**
 * Created by landschoot on 11/12/16.
 */
public class MessageService {
    public static MessageService instance = null;
    private MessageMaper messageMapper;

    private MessageService(){
        messageMapper = MessageMaper.getInstance();
    }

    public static MessageService getInstance() {
        if (instance == null) {
            instance = new MessageService();
        }
        return instance;
    }

}
