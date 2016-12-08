package domain;

import domain.enums.Role;

/**
 * Created by landschoot on 02/12/16.
 */
public interface IUser extends IDomainObject {
    public String getFirstname();

    public String getLastname();

    public Role getRole();

    public String getLogin();

    public void setFirstname(String firstname);

    public void setLastname(String lastname);

    public void setRole(Role role);

    public void setLogin(String login);
}
