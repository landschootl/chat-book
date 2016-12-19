package domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Classe représentant une discussion
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
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

    /**
     * Ajout d'un utilisateur à la discussion.
     * @param user
     */
    public void addUser(IUser user){
        users.add(user);
    }

    /**
     * Suppression d'un utilisateur à la discussion.
     * @param idUser
     */
    public void removeUser(int idUser){
        for(int i=0; i<users.size(); i++){
            if(users.get(i).getId() == idUser) {
                users.remove(i);
            }
        }
    }

    /**
     * Méthode pour vérifier si un utilisateur donné est dans la discussion.
     * @param user
     * @return si l'utilisateur est dans la conversation
     */
    public boolean containUser(IUser user){
        for(IUser u : users){
            if(u.getId() == user.getId()){
                return true;
            }
        }
        return false;
    }

    /**
     * Ajout d'un message dans la discussion.
     * @param message
     */
    public void addMessage(Message message) {
        messages.add(message);
    }
}