package domain;

import domain.enums.ERole;
import lombok.Builder;
import lombok.Getter;
import persistence.uow.Observer;
import persistence.uow.Visitor;

import java.util.List;

@Builder
@Getter
public class User implements IUser{
    List<Observer> obs;

    private int id;
    private String login;
    private String firstname;
    private String lastname;
    private String password;
    private ERole role;

    @Override
    public String toString() {
        return firstname + " " + lastname;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public void addObserver(Observer o) {
        obs.add(o);
    }

    @Override
    public void notif(Object o) {
        for (Observer ob : obs)
            ob.action(this);
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
        notif(null);
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
        notif(null);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(ERole role) {
        this.role = role;
        notif(null);
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
        notif(null);
    }

    public boolean isAdmin(){
        return ERole.USER_ADMIN.equals(role);
    }
}
