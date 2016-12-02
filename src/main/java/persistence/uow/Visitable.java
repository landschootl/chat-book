package persistence.uow;

/**
 * Created by lauthieb on 02/12/2016.
 */
public interface Visitable {
    void accept(Visitor v);
}
