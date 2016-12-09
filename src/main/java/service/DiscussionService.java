package service;

import domain.Discussion;
import domain.User;
import persistence.db.DiscussionMapper;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by landschoot on 04/11/16.
 * Permet la gestion des discussiones de discutions.
 */
public class DiscussionService {
    public static DiscussionService instance = null;
    private DiscussionMapper discussionMapper;

    public DiscussionService(){
        discussionMapper = DiscussionMapper.getInstance();
    }

    public static DiscussionService getInstance() {
        if (instance == null) {
            instance = new DiscussionService();
        }
        return instance;
    }

    public List<Discussion> findAll() throws SQLException {
        return discussionMapper.findAll();
    }

    public List<Discussion> findByUser(User user) throws SQLException {
        return discussionMapper.findByUser(user);
    }

    public Discussion findById(int id){
        return discussionMapper.findById(id);
    }

    public boolean addUser(User user){
        return discussionMapper.addUser(user);
    }

    public boolean removeUser(User user){
        return discussionMapper.removeUser(user);
    }

    public Discussion update(Discussion discussion){
        return discussionMapper.update(discussion);
    }

    public Discussion create(Discussion discussion){
        return discussionMapper.create(discussion);
    }

    public boolean delete(Discussion discussion){
        return discussionMapper.remove(discussion);
    }
}