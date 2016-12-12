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
 * Created by landschoot on 04/11/16.
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

    public List<Discussion> findAll() throws SQLException {
        List <Discussion> discussions = new ArrayList<>();
        ResultSet rs = statement.executeQuery(this.bundle.getString("select.discussions.all"));

        while(rs.next()) {
            discussions.add(createDiscussion(rs));
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
            discussions.add(createDiscussion(rs));
        }
        rs.close();
        return discussions;
    }

    public Discussion findById(int id) {
        throw new NO_IMPLEMENT();
    }

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

    public Discussion update(Discussion discussion) {
        try {
            PreparedStatement preparedStatement = db.prepareStatement(this.bundle.getString("update.discussion"));
            preparedStatement.setInt(1, discussion.getMod().getId());
            preparedStatement.setString(2, discussion.getName());
            preparedStatement.setInt(3, discussion.getId());
            preparedStatement.executeUpdate();
            List<IUser> oldUsers = userService.findByDiscussion(discussion);
            List<IUser> existUsers = new ArrayList<>();
            for(IUser oldUser : oldUsers){
                if(!discussion.containUser(oldUser)){
                    existUsers.add(discussion.getUser(oldUser.getId()));
                } else {
                    removeUser(discussion, oldUser);
                }
            }
            for(IUser user : discussion.getUsers()){
                if(!existUsers.contains(user)) {
                    addUser(discussion, user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discussion;
    }

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

    public void remove(Discussion discussion) {
        try {
            PreparedStatement preparedStatement = db.prepareStatement(this.bundle.getString("delete.discussion.by.identifiant"));
            preparedStatement.setInt(1, discussion.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
