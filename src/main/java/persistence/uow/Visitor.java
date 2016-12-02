package persistence.uow;

import domain.IDomainObject;
import domain.User;

/**
 * Created by lauthieb on 02/12/2016.
 */
public abstract class Visitor {
    public void visit(IDomainObject o) {
        o.accept(this);
    }
    abstract public void visit(User u);
}
