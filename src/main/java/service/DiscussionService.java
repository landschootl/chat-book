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

    private DiscussionService(){
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

    public Discussion update(Discussion discussion){
        return discussionMapper.update(discussion);
    }

    public Discussion create(Discussion discussion){
        return discussionMapper.create(discussion);
    }

    public void delete(Discussion discussion){
        discussionMapper.remove(discussion);
    }

    public Discussion saveDiscussion(Discussion discussion) {
        if(discussion.getId() == 0){
            return discussionMapper.create(discussion);
        } else {
            return discussionMapper.update(discussion);
        }
    }
}
