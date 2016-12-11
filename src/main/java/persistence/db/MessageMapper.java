package persistence.db;

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
}
