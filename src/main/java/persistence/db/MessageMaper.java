package persistence.db;

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
}
