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
        for(int i=0; i<users.size(); i++){
            if(users.get(i).getId() == idUser) {
                users.remove(i);
            }
        }
    }

    public boolean containUser(IUser user){
        for(IUser u : users){
            if(u.getId() == user.getId()){
                return true;
            }
        }
        return false;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public IUser getUser(int id) {
        for(IUser user : users){
            if(user.getId() == user.getId()){
                return user;
            }
        }
        throw new RuntimeException("La discussion ne possède pas cette personne");
    }
}