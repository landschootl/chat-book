package service;

import domain.IUser;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe représentant le service de recherche d'utilisateurs.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
@Data
public class SearchService {
    public static SearchService instance = null;

    private UserService userService;

    private SearchService() {
        this.userService = userService.getInstance();
    }

    public static SearchService getInstance() {
        if (instance == null) {
            instance = new SearchService();
        }
        return instance;
    }

    /**
     * Rechercher un utilisateur en fonction de son nom / prénom.
     * @param lastname
     * @param firstname
     * @return
     */
    public List<IUser> searchUsers(String lastname, String firstname) {
        List<IUser> users = this.userService.getUsers();

        return users
                .stream()
                .filter((IUser user) ->
                        user.getLastname().toUpperCase().startsWith(lastname.toUpperCase()) &&
                        user.getFirstname().toUpperCase().startsWith(firstname.toUpperCase()))
                .collect(Collectors.toList());
    }
}
