package domain;

import persistence.uow.Observable;
import persistence.uow.Visitable;

/**
 * Created by lauthieb on 02/12/2016.
 */
public interface IDomainObject extends Observable,Visitable {
}
