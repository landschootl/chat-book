package domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Created by landschoot on 11/12/16.
 */
@Data
@Builder
public class Message {
    private int id;
    private int idConnection;
    private IUser user;
    private String message;
    private boolean accused;
    private boolean priority;
    private LocalDate expiration;
    private boolean code;
}
