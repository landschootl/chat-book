package persistence.uow;

/**
 * Interface décrivant un visitable.
 *
 * @author Ludovic LANDSCHOOT & Laurent THIEBAULT
 */
public interface Visitable {
    void accept(Visitor v);
}
