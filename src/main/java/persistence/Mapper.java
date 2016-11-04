package persistence;

import net.rakugakibox.util.YamlResourceBundle;

import java.sql.Connection;
import java.util.ResourceBundle;

/**
 * Created by landschoot on 04/11/16.
 */
public abstract class Mapper {
    protected Connection db;
    protected ResourceBundle bundle;

    public Mapper(){
        this.db = SingletonDB.getInstance().getDb();
        this.bundle = ResourceBundle.getBundle("db/requests", YamlResourceBundle.Control.INSTANCE);
    }
}
