package domain;

import lombok.Builder;
import lombok.Data;
import persistence.uow.Observer;
import persistence.uow.Visitor;

import java.util.List;

@Data
@Builder
public class User implements IDomainObject {
    List<Observer> obs;

    private int id;
    private String login;
    private String firstname;
    private String lastname;
    private String role;

    @Override
    public String toString() {
        return firstname + " " +
                lastname + " - " +
                getRole();
    }

    public String getRole() {
        switch(role) {
            case "USER_ADMIN":
                return "Administrateur";
            case "USER_DEFAULT":
                return "Utilisateur";
        }
        return "Utilisateur";
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
}
