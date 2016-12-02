package domain;

import domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import persistence.uow.Observer;
import persistence.uow.Visitor;

import java.util.List;

@Builder
@Getter
public class User implements IDomainObject {
    List<Observer> obs;

    private int id;
    private String login;
    private String firstname;
    private String lastname;
    private Role role;

    @Override
    public String toString() {
        return firstname + " " + lastname;
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

    public void setFirstname(String firstname) {
        this.firstname = firstname;
        notif();
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
        notif();
    }

    public void setRole(Role role) {
        this.role = role;
        notif();
    }
}
