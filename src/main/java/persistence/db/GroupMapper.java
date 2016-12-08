package persistence.db;

import domain.Group;
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
public class GroupMapper extends Mapper {
    public static GroupMapper instance = null;
    private Map<Integer, Group> cache;

    public GroupMapper(){
        super();
        this.cache = new HashMap<>();
    }

    public static GroupMapper getInstance() {
        if (instance == null) {
            instance = new GroupMapper();
        }
        return instance;
    }

    public List<Group> findAll() throws SQLException {
        List <Group> groups = new ArrayList<>();
        ResultSet rs = statement.executeQuery(this.bundle.getString("select.groups.all"));

        while(rs.next()) {
            groups.add(Group.builder()
                    .id(rs.getInt("id"))
                    .mod(new VirtualProxyBuilder<>(IUser.class, new UserFactory(rs.getString("id_mod"))).getProxy())
                    .name(rs.getString("name"))
                    .build());
        }
        rs.close();

        return groups;
    }

    public List<Group> findByUser(User user) throws SQLException {
        List <Group> groups = new ArrayList<>();
        preparedStatement = db.prepareStatement(this.bundle.getString("select.groups.by.user"));
        preparedStatement.setInt(1, user.getId());
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()) {
            groups.add(Group.builder()
                    .id(rs.getInt("id"))
                    .mod(new VirtualProxyBuilder<>(IUser.class, new UserFactory(rs.getString("id_mod"))).getProxy())
                    .name(rs.getString("name"))
                    .build());
        }
        rs.close();

        return groups;
    }

    public Group findById(int id) {
        throw new NO_IMPLEMENT();
    }

    public boolean addUser(User user) {
        throw new NO_IMPLEMENT();
    }

    public boolean removeUser(User user) {
        throw new NO_IMPLEMENT();
    }

    public Group update(Group group) {
        throw new NO_IMPLEMENT();
    }

    public Group create(Group group) {
        throw new NO_IMPLEMENT();
    }

    public boolean remove(Group group) {
        throw new NO_IMPLEMENT();
    }
}
