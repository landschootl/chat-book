package persistence.db;

import net.rakugakibox.util.YamlResourceBundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Classe abstraite représentant un mapper de l'application.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
public abstract class Mapper {
    protected Connection db;
    protected ResourceBundle bundle;
    protected Statement statement;
    protected PreparedStatement preparedStatement;

    public Mapper() {
        this.db = SingletonDB.getInstance().getDb();
        this.bundle = ResourceBundle.getBundle("db/requests", YamlResourceBundle.Control.INSTANCE);
        try {
            this.statement = db.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
