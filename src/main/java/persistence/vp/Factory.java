package persistence.vp;

import persistence.db.NoDataFoundException;

import java.sql.SQLException;

/**
 * Interface décrivant une factory
 *
 * @param <T> le type d'objet que crééra la factory
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT
 */
public interface Factory<T> {
    /**
     * Créer le type T.
     * @return
     * @throws SQLException
     */
    T create() throws SQLException, NoDataFoundException;
}
