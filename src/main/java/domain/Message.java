package domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Classe représentant un message
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
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
