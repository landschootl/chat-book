package persistence.db;

/**
 * Created by landschoot on 06/12/16.
 */
public class NoDataFoundException extends Throwable {
    public NoDataFoundException(String text) {
        super(text);
    }
}
