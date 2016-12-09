package domain;

import lombok.Builder;
import lombok.Getter;
import persistence.uow.Observer;
import persistence.uow.Visitor;

import java.util.List;

/**
 * Created by landschoot on 04/11/16.
 */
@Getter
@Builder
public class Discussion implements IDomainObject{
    List<Observer> obs;

    private int id;
    private IUser mod;
    private String name;

    @Override
    public String toString() {
        return "nom : "+name+" | moderateur : "+mod.getLogin();
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public void add(Observer o) {
        obs.add(o);
    }

    @Override
    public void notif() {
        for (Observer o : obs)
            o.action(this);
    }

    public void setId(int id) {
        this.id = id;
        notif();
    }

    public void setMod(IUser mod) {
        this.mod = mod;
        notif();
    }

    public void setName(String name) {
        this.name = name;
        notif();
    }
}
