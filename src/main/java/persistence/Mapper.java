package persistence;

import net.rakugakibox.util.YamlResourceBundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by landschoot on 04/11/16.
 */
public abstract class Mapper {
    protected Connection db;
    protected ResourceBundle bundle;
    protected Statement statement;
    protected PreparedStatement preparedStatement;

    public Mapper(){
        this.db = SingletonDB.getInstance().getDb();
        this.bundle = ResourceBundle.getBundle("db/requests", YamlResourceBundle.Control.INSTANCE);
        try {
            this.statement = db.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
