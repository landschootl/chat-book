package persistence.db;

import domain.Discussion;
import domain.IUser;
import domain.User;
import org.omg.CORBA.NO_IMPLEMENT;
import persistence.vp.UserFactory;
import persistence.vp.VirtualProxyBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by landschoot on 04/11/16.
 */
public class DiscussionMapper extends Mapper {
    public static DiscussionMapper instance = null;
    private Map<Integer, Discussion> cache;

    public DiscussionMapper(){
        super();
        this.cache = new HashMap<>();
    }

    public static DiscussionMapper getInstance() {
        if (instance == null) {
            instance = new DiscussionMapper();
        }
        return instance;
    }

    public List<Discussion> findAll() throws SQLException {
        List <Discussion> discussions = new ArrayList<>();
        ResultSet rs = statement.executeQuery(this.bundle.getString("select.discussions.all"));

        while(rs.next()) {
            discussions.add(Discussion.builder()
                    .id(rs.getInt("id"))
                    .mod(new VirtualProxyBuilder<>(IUser.class, new UserFactory(rs.getString("id_mod"))).getProxy())
                    .name(rs.getString("name"))
                    .build());
        }
        rs.close();

        return discussions;
    }

    public List<Discussion> findByUser(User user) throws SQLException {
        List <Discussion> discussions = new ArrayList<>();
        preparedStatement = db.prepareStatement(this.bundle.getString("select.discussions.by.user"));
        preparedStatement.setInt(1, user.getId());
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()) {
            discussions.add(Discussion.builder()
                    .id(rs.getInt("id"))
                    .mod(new VirtualProxyBuilder<>(IUser.class, new UserFactory(rs.getString("id_mod"))).getProxy())
                    .name(rs.getString("name"))
                    .build());
        }
        rs.close();

        return discussions;
    }

    public Discussion findById(int id) {
        throw new NO_IMPLEMENT();
    }

    public boolean addUser(User user) {
        throw new NO_IMPLEMENT();
    }

    public boolean removeUser(User user) {
        throw new NO_IMPLEMENT();
    }

    public Discussion update(Discussion discussion) {
        throw new NO_IMPLEMENT();
    }

    public Discussion create(Discussion discussion) {
        throw new NO_IMPLEMENT();
    }

    public boolean remove(Discussion discussion) {
        throw new NO_IMPLEMENT();
    }
}