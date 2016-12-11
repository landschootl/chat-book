package domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Created by landschoot on 04/11/16.
 */
@Getter
@Builder
public class Discussion {
    private int id;
    private IUser mod;
    private String name;
    private List<IUser> users;

    @Override
    public String toString() {
        return "nom : "+name+" | moderateur : "+mod.getLogin();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMod(IUser mod) {
        this.mod = mod;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(List<IUser> users) {
        this.users = users;
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

}