package persistence.db;

import domain.Discussion;
import domain.IUser;
import domain.User;
import org.omg.CORBA.NO_IMPLEMENT;
import persistence.vp.UserFactory;
import persistence.vp.VirtualProxyBuilder;
import service.MessageService;
import service.UserService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant le mapper d'une discussion.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
public class DiscussionMapper extends Mapper {
    public static DiscussionMapper instance = null;

    private UserService userService;
    private MessageService messageService;

    public DiscussionMapper(){
        super();
        this.userService = UserService.getInstance();
        this.messageService = MessageService.getInstance();
    }

    public static DiscussionMapper getInstance() {
        if (instance == null) {
            instance = new DiscussionMapper();
        }
        return instance;
    }

    /**
     * Retourne la liste de l'ensemble des discussions.
     * @return
     * @throws SQLException
     */
    public List<Discussion> findAll() throws SQLException {
        List <Discussion> discussions = new ArrayList<>();
        ResultSet rs = statement.executeQuery(this.bundle.getString("select.discussions.all"));

        while(rs.next()) {
            discussions.add(createDiscussion(rs));
        }
        rs.close();

        return discussions;
    }

    /**
     * Retourne la liste des discussions d'un utilisateur.
     * @param user
     * @return
     * @throws SQLException
     */
    public List<Discussion> findByUser(User user) throws SQLException {
        List <Discussion> discussions = new ArrayList<>();
        preparedStatement = db.prepareStatement(this.bundle.getString("select.discussions.by.user"));
        preparedStatement.setInt(1, user.getId());
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()) {
            discussions.add(createDiscussion(rs));
        }
        rs.close();
        return discussions;
    }

    /**
     * Retourne une discussion en fonction de son id.
     * @param id
     * @return
     */
    public Discussion findById(int id) {
        throw new NO_IMPLEMENT();
    }

    /**
     * Ajoute un utilisateur à une discussion.
     * @param discussion
     * @param user
     */
    public void addUser(Discussion discussion, IUser user) {
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("create.discussion.user"));
            preparedStatement.setInt(1, discussion.getId());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Supprime un utilisateur d'une discussion.
     * @param discussion
     * @param user
     */
    public void removeUser(Discussion discussion, IUser user) {
        try {
            preparedStatement = db.prepareStatement(this.bundle.getString("delete.discussion.user"));
            preparedStatement.setInt(1, discussion.getId());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mise à jour d'une discussion.
     * @param discussion
     * @return
     */
    public Discussion update(Discussion discussion) {
        try {
            PreparedStatement preparedStatement = db.prepareStatement(this.bundle.getString("update.discussion"));
            preparedStatement.setInt(1, discussion.getMod().getId());
            preparedStatement.setString(2, discussion.getName());
            preparedStatement.setInt(3, discussion.getId());
            preparedStatement.executeUpdate();
            List<IUser> oldUsers = userService.findByDiscussion(discussion);
            for(IUser user : discussion.getUsers()){
                boolean userExist = false;
                for (int i=0; i<oldUsers.size(); i++) {
                    IUser oldUser = oldUsers.get(i);
                    if (user.getId() == oldUser.getId()) {
                        oldUsers.remove(i);
                        userExist = true;
                    }
                }
                if (!userExist){
                    addUser(discussion, user);
                }
            }
            for(IUser oldUser : oldUsers){
                removeUser(discussion, oldUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discussion;
    }

    /**
     * Créé une discussion en base de données.
     * @param discussion
     * @return
     */
    public Discussion create(Discussion discussion) {
        try {
            PreparedStatement preparedStatement = db.prepareStatement(this.bundle.getString("create.discussion"), PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, discussion.getMod().getId());
            preparedStatement.setString(2, discussion.getName());
            preparedStatement.executeUpdate();
            ResultSet resultId = preparedStatement.getGeneratedKeys();
            resultId.next();
            discussion.setId(resultId.getInt(1));
            for(IUser user : discussion.getUsers()){
                addUser(discussion, user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discussion;
    }

    /**
     * Supprime une discussion.
     * @param discussion
     */
    public void remove(Discussion discussion) {
        try {
            PreparedStatement preparedStatement = db.prepareStatement(this.bundle.getString("delete.discussion.by.identifiant"));
            preparedStatement.setInt(1, discussion.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Créé une discussion.
     * @param rs
     * @return
     * @throws SQLException
     */
    private Discussion createDiscussion(ResultSet rs) throws SQLException {
        Discussion discussion = Discussion.builder()
                .id(rs.getInt("id"))
                .mod(new VirtualProxyBuilder<>(IUser.class, new UserFactory(rs.getString("id_mod"))).getProxy())
                .name(rs.getString("name"))
                .build();
        discussion.setUsers(userService.findByDiscussion(discussion));
        discussion.setMessages(messageService.findByDiscussion(discussion));
        return discussion;
    }
}
