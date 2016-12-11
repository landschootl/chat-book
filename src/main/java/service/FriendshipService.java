package service;

import domain.Friendship;
import domain.IUser;
import lombok.Data;
import persistence.db.FriendshipMapper;

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

    public Friendship findFriendship(IUser user1, IUser user2) {
        return friendshipMapper.find(user1, user2);
    }
}
