package domain;

import lombok.Builder;
import lombok.Data;

/**
 * Classe représentant une amitié entre deux utilisateurs
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
@Data
@Builder
public class Friendship {
    private int id;
    private IUser user1;
    private IUser user2;
    private boolean confirmed;
}
