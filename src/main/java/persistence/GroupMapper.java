package persistence;

import domain.Group;
import domain.User;
import net.rakugakibox.util.YamlResourceBundle;
import org.omg.CORBA.NO_IMPLEMENT;
import service.GroupService;

import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by landschoot on 04/11/16.
 */
public class GroupMapper extends Mapper {
    public static GroupMapper instance = null;

    public GroupMapper(){
        super();
    }

    public static GroupMapper getInstance() {
        if (instance == null) {
            instance = new GroupMapper();
        }
        return instance;
    }

    public List<Group> findAll() {
        throw new NO_IMPLEMENT();
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
