package persistence;

import domain.Group;
import domain.User;
import net.rakugakibox.util.YamlResourceBundle;
import org.omg.CORBA.NO_IMPLEMENT;
import service.GroupService;
import service.UserService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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
        ResultSet rs = statement.executeQuery(this.bundle.getString("select.all.groups"));

        while(rs.next()) {
            groups.add(Group.builder()
                    .id(rs.getInt(1))
                    .admin(rs.getString(2))
                    .name(rs.getString(3))
                    .build());
        }
        rs.close();

        return groups;
    }

    public List<Group> findByUser() throws SQLException {
        List <Group> groups = new ArrayList<>();
        ResultSet rs = statement.executeQuery(this.bundle.getString("select.by.user"));

        while(rs.next()) {
            groups.add(Group.builder()
                    .id(rs.getInt(1))
                    .admin(rs.getString(2))
                    .name(rs.getString(3))
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
