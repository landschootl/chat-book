package persistence.uow;

import domain.User;
import persistence.db.UserMapper;

/**
 * Classe représentant un visiteur permettant de faire les mises à jour en base de données.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
public class Committer extends Visitor {

    @Override
    public void visit(User u) {
        UserMapper.getInstance().update(u);
    }
}
