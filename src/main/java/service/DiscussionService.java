package service;

import domain.Discussion;
import domain.User;
import persistence.db.DiscussionMapper;

import java.sql.SQLException;
import java.util.List;


/**
 * Classe représentant le service d'une discussion.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
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

    /**
     * Retourne la liste de l'ensemble des discussions.
     * @return
     * @throws SQLException
     */
    public List<Discussion> findAll() throws SQLException {
        return discussionMapper.findAll();
    }

    /**
     * Retourne la liste des discussions d'un utilisateur.
     * @param user
     * @return
     * @throws SQLException
     */
    public List<Discussion> findByUser(User user) throws SQLException {
        return discussionMapper.findByUser(user);
    }

    /**
     * Retourne une discussion en fonction de son id.
     * @param id
     * @return
     */
    public Discussion findById(int id){
        return discussionMapper.findById(id);
    }

    /**
     * Mise à jour d'une discussion.
     * @param discussion
     * @return
     */
    public Discussion update(Discussion discussion){
        return discussionMapper.update(discussion);
    }

    /**
     * Créé une discussion en base de données.
     * @param discussion
     * @return
     */
    public Discussion create(Discussion discussion){
        return discussionMapper.create(discussion);
    }

    /**
     * Supprime une discussion.
     * @param discussion
     */
    public void delete(Discussion discussion){
        discussionMapper.remove(discussion);
    }
}
