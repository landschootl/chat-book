package service;

import domain.IUser;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

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
