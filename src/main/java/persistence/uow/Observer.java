package persistence.uow;

import domain.IDomainObject;

/**
 * Interface décrivant un observateur.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
public interface Observer {
    void action(IDomainObject o);
    void action(Object o);
}