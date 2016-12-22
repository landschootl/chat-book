package persistence.db;

/**
 * Classe d'exception métier lorsque des données sont absentes en base de données.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
public class NoDataFoundException extends Throwable {
    public NoDataFoundException(String text) {
        super(text);
    }
}
