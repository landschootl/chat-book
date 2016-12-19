package domain;

import persistence.uow.Observable;
import persistence.uow.Visitable;

/**
 * Interface décrivant un objet du domaine.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
public interface IDomainObject extends Observable,Visitable {
}
