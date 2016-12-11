package domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friendship {
    private IUser user1;
    private IUser user2;
    private boolean confirmed;
}
