package domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
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
}
