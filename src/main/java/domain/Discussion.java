package domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by landschoot on 04/11/16.
 */
@Data
@Builder
public class Discussion {
    private int id;
    private IUser mod;
    private String name;
    private List<IUser> users;
    private List<Message> messages;

    @Override
    public String toString() {
        return "nom : "+name+" | moderateur : "+mod.getLogin();
    }

    public void addUser(IUser user){
        users.add(user);
    }

    public void removeUser(int idUser){
        for(IUser user : users){
            if(user.getId() == idUser) {
                users.remove(user);
            }
        }
    }

    public void addMessage(Message message) {
        messages.add(message);
    }
}