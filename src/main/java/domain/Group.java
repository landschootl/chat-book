package domain;

import lombok.Builder;
import lombok.Data;

/**
 * Created by landschoot on 04/11/16.
 */
@Data
@Builder
public class Group {
    private int id;
    private String name;
    private User admin;
}
