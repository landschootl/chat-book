package persistence.db;

import net.rakugakibox.util.YamlResourceBundle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Classe singleton servant à la connexion à une base de données dont la config est située dans le fichier resources/tp3.yml
 *
 * @author Ludovic LANDSCHOOT & Laurent THIEBAULT
 */
public class SingletonDB {

    public static SingletonDB instance = null;

    private static String DB_DRIVER;
    private static String DB_HOTE;
    private static String DB_BASE;
    private static String DB_LOGIN;
    private static String DB_PASSWORD;

    private Connection db;

    public static SingletonDB getInstance() {
        if (instance == null) {
            instance = new SingletonDB();
        }
        return instance;
    }

    private SingletonDB() {
        loadConfig();
        connect();
    }

    private void loadConfig() {
        ResourceBundle bundle = ResourceBundle.getBundle("config", YamlResourceBundle.Control.INSTANCE);
        this.DB_DRIVER = bundle.getString("db.driver");
        this.DB_HOTE = bundle.getString("db.hote");
        this.DB_BASE = bundle.getString("db.base");
        this.DB_LOGIN = bundle.getString("db.login");
        this.DB_PASSWORD = bundle.getString("db.password");
    }

    public Boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.db = DriverManager.getConnection(DB_DRIVER + "://" + DB_HOTE + "/" + DB_BASE, DB_LOGIN, DB_PASSWORD);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void close() {
        try {
            this.db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getDb() {
        return db;
    }
}
