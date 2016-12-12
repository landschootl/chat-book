package domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime dateExpedition;
    private LocalDateTime dateAccused;
    private boolean accused;
    private boolean priority;
    private LocalDate expiration;
    private boolean code;
}
