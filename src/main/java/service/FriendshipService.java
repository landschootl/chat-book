package service;

import domain.Friendship;
import domain.IUser;
import lombok.Data;
import persistence.db.FriendshipMapper;

import java.util.List;

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

    public void create(Friendship friendship) {
        friendshipMapper.create(friendship);
    }

    public void delete(Friendship friendship) {
        friendshipMapper.delete(friendship);
    }

    public Friendship find(IUser user1, IUser user2) {
        return friendshipMapper.find(user1, user2);
    }

    public List<Friendship> findWaitingFriendships(IUser user) {
        return friendshipMapper.findWaitingFriendships(user);
    }

    public void acceptFriendship(Friendship friendship) {
        friendshipMapper.acceptFriendship(friendship);
    }

    public void refuseFriendship(Friendship friendship) {
        friendshipMapper.refuseFriendship(friendship);
    }
}
