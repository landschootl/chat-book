package persistence.uow;

import domain.IDomainObject;
import domain.User;

/**
 * Interface décrivant un visiteur.
 *
 * @author Ludovic LANDSCHOOT & Laurent THIEBAULT
 */
public abstract class Visitor {
    public void visit(IDomainObject o) {
        o.accept(this);
    }
    abstract public void visit(User u);
}
