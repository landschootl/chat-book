package service;

import domain.Friendship;
import domain.IUser;
import lombok.Data;
import persistence.db.FriendshipMapper;

import java.util.List;

/**
 * Classe représentant le service d'une amitié.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
@Data
public class FriendshipService {
    public static FriendshipService instance = null;

    private FriendshipMapper friendshipMapper;

    private FriendshipService() {
        this.friendshipMapper = FriendshipMapper.getInstance();
    }

    public static FriendshipService getInstance() {
        if (instance == null) {
            instance = new FriendshipService();
        }
        return instance;
    }

    /**
     * Création d'une amitié entre deux utilisateurs.
     * @param friendship
     */
    public void create(Friendship friendship) {
        friendshipMapper.create(friendship);
    }

    /**
     * Supprimer une amitié entre deux utilisateurs.
     * @param friendship
     */
    public void delete(Friendship friendship) {
        friendshipMapper.delete(friendship);
    }

    /**
     * Retourne une amitié entre deux utilisateurs.
     * @param user1
     * @param user2
     * @return
     */
    public Friendship find(IUser user1, IUser user2) {
        return friendshipMapper.find(user1, user2);
    }

    /**
     * Retourne la liste des demandes d'amitié en attente d'un utilisateur.
     * @param user
     * @return
     */
    public List<Friendship> findWaitingFriendships(IUser user) {
        return friendshipMapper.findWaitingFriendships(user);
    }

    /**
     * Accepter une demande d'amitié en attente.
     * @param friendship
     */
    public void acceptFriendship(Friendship friendship) {
        friendshipMapper.acceptFriendship(friendship);
    }

    /**
     * Refuser une demande d'amitié en attente.
     * @param friendship
     */
    public void refuseFriendship(Friendship friendship) {
        friendshipMapper.refuseFriendship(friendship);
    }
}
