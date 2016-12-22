package persistence.db;

import domain.Friendship;
import domain.IUser;
import persistence.vp.UserFactory;
import persistence.vp.VirtualProxyBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant le mapper d'une amitié.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
public class FriendshipMapper extends Mapper {
    public static FriendshipMapper instance = null;

    private FriendshipMapper() {
        super();
    }

    public static FriendshipMapper getInstance() {
        if (instance == null) {
            instance = new FriendshipMapper();
        }
        return instance;
    }

    /**
     * Création d'une amitié entre deux utilisateurs.
     * @param friendship
     */
    public void create(Friendship friendship) {
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("create.friendship"));
            preparedStatement.setInt(1, friendship.getUser1().getId());
            preparedStatement.setInt(2, friendship.getUser2().getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne une amitié entre deux utilisateurs.
     * @param user1
     * @param user2
     * @return
     */
    public Friendship find(IUser user1, IUser user2) {
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("select.friendship"));
            preparedStatement.setInt(1, user1.getId());
            preparedStatement.setInt(2, user2.getId());
            preparedStatement.setInt(3, user2.getId());
            preparedStatement.setInt(4, user1.getId());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return Friendship.builder()
                        .user1(user1)
                        .user2(user2)
                        .confirmed(rs.getBoolean("confirmed")).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retourne la liste des demandes d'amitié en attente d'un utilisateur.
     * @param user
     * @return
     */
    public List<Friendship> findWaitingFriendships(IUser user) {
        List<Friendship> waitingFriendships = new ArrayList<>();

        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("select.friendship.waiting"));
            preparedStatement.setInt(1, user.getId());

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                waitingFriendships.add(
                    Friendship.builder()
                            .id(rs.getInt("id"))
                            .user1(new VirtualProxyBuilder<>(IUser.class, new UserFactory(rs.getString("id_user1"))).getProxy())
                            .user2(new VirtualProxyBuilder<>(IUser.class, new UserFactory(rs.getString("id_user2"))).getProxy())
                            .confirmed(rs.getBoolean("confirmed")).build()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return waitingFriendships;
    }

    /**
     * Accepter une demande d'amitié en attente.
     * @param friendship
     */
    public void acceptFriendship(Friendship friendship) {
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("update.friendship.accept"));
            preparedStatement.setInt(1, friendship.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refuser une demande d'amitié en attente.
     * @param friendship
     */
    public void refuseFriendship(Friendship friendship) {
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("delete.friendship.refuse"));
            preparedStatement.setInt(1, friendship.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Supprimer une amitié entre deux utilisateurs.
     * @param friendship
     */
    public void delete(Friendship friendship) {
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("delete.friendship"));
            preparedStatement.setInt(1, friendship.getUser1().getId());
            preparedStatement.setInt(2, friendship.getUser2().getId());
            preparedStatement.setInt(3, friendship.getUser2().getId());
            preparedStatement.setInt(4, friendship.getUser1().getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
