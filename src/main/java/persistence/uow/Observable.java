package persistence.uow;

/**
 * Interface décrivant un observable.
 *
 * @author Ludovic LANDSCHOOT & Laurent THIEBAULT
 */
public interface Observable {
    void addObserver(Observer o);
    void notif(Object o);
}