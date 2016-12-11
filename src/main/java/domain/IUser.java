package domain;

import domain.enums.ERole;

/**
 * Created by landschoot on 02/12/16.
 */
public interface IUser extends IDomainObject {
    public String getFirstname();

    public String getLastname();

    public ERole getRole();

    public String getLogin();

    public void setFirstname(String firstname);

    public void setLastname(String lastname);

    public void setRole(ERole role);

    public void setLogin(String login);

    public int getId();
}
