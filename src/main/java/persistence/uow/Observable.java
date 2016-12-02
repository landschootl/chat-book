package persistence.uow;

/**
 * Interface décrivant un observable.
 *
 * @author Ludovic LANDSCHOOT & Laurent THIEBAULT
 */
public interface Observable {
    void add(Observer o);
    void notif();
}