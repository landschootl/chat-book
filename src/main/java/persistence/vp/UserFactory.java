package persistence.vp;

import domain.IUser;
import persistence.db.UserMapper;

import java.sql.SQLException;

/**
 * Classe représentant une factory pour un utilisateur.
 *
 * @author Ludovic LANDSCHOOT & Laurent THIEBAULT
 */
public class UserFactory implements Factory<IUser> {

    String identifiant;
    UserMapper userMapper;

    public UserFactory(String identifiant) {
        this.identifiant = identifiant;
        this.userMapper = UserMapper.getInstance();
    }

    @Override
    public IUser create() throws SQLException {
        return userMapper.findByIdentifiant(this.identifiant);
    }
}
