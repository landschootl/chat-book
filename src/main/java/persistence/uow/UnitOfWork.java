package persistence.uow;

import domain.IDomainObject;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe représentant l'UnitOfWork.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT (via Clément BALLABRIGA)
 */
@Getter
public class UnitOfWork implements Observer {
    public static UnitOfWork inst = null;
    Set<IDomainObject> dirty;

    public UnitOfWork() {
        dirty = new HashSet<>();
    }

    public static UnitOfWork getInstance() {
        if (inst == null)
            inst = new UnitOfWork();
        return inst;
    }

    /**
     * Ajoute un utilisateur à modifier.
     * @param o
     */
    public void action(IDomainObject o) {
        dirty.add(o);
    }

    @Override
    public void action(Object o) {

    }

    @Override
    public void action(Object crud, Object element) {

    }

    /**
     * Effectue les modifications sur tous les utilisateurs de la liste.
     */
    public void commit() {
        Visitor v = new Committer();
        for (IDomainObject o : dirty) {
            v.visit(o);
        }
        dirty.clear();
    }

    /**
     * Reinitialise à vide la liste des utilisateurs à modifier.
     */
    public void rollback() {
        dirty.clear();
    }
}