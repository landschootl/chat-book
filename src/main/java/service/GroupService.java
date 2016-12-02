package service;

import domain.Group;
import domain.User;
import persistence.db.GroupMapper;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by landschoot on 04/11/16.
 * Permet la gestion des groupes de discutions.
 */
public class GroupService {
    public static GroupService instance = null;
    private GroupMapper groupMapper;

    public GroupService(){
        groupMapper = GroupMapper.getInstance();
    }

    public static GroupService getInstance() {
        if (instance == null) {
            instance = new GroupService();
        }
        return instance;
    }

    public List<Group> findAll() throws SQLException {
        return groupMapper.findAll();
    }

    public List<Group> findByUser() throws SQLException {
        return groupMapper.findByUser();
    }

    public Group findById(int id){
        return groupMapper.findById(id);
    }

    public boolean addUser(User user){
        return groupMapper.addUser(user);
    }

    public boolean removeUser(User user){
        return groupMapper.removeUser(user);
    }

    public Group update(Group group){
        return groupMapper.update(group);
    }

    public Group create(Group group){
        return groupMapper.create(group);
    }

    public boolean delete(Group group){
        return groupMapper.remove(group);
    }
}
