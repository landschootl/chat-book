package domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

/**
 * Created by landschoot on 11/12/16.
 */
@Getter
@Builder
public class Message {
    private int id;
    private int id_connection;
    private int id_user;
    private String message;
    private boolean accused;
    private boolean priority;
    private Date date;
    private boolean code;
}
