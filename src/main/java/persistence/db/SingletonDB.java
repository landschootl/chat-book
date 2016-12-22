package persistence.db;

import net.rakugakibox.util.YamlResourceBundle;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Classe singleton servant à la connexion à la base de données.
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

    /**
     * Chargement de la configuration dans le fichier resources/config.yml.
     */
    private void loadConfig() {
        ResourceBundle bundle = ResourceBundle.getBundle("config", YamlResourceBundle.Control.INSTANCE);
        this.DB_DRIVER = bundle.getString("db.driver");
        this.DB_HOTE = bundle.getString("db.hote");
        this.DB_BASE = bundle.getString("db.base");
        this.DB_LOGIN = bundle.getString("db.login");
        this.DB_PASSWORD = bundle.getString("db.password");
    }

    /**
     * Permet la connexion à la base de données.
     * @return
     */
    public Boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.db = DriverManager.getConnection(DB_DRIVER + "://" + DB_HOTE + "/" + DB_BASE, DB_LOGIN, DB_PASSWORD);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "La connexion à la base de données a echouée.");
        }
        return false;
    }

    /**
     * Fermeture de la connexion à la base de données.
     */
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
