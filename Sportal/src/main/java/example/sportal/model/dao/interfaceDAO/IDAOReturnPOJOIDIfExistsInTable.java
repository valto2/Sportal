package example.sportal.model.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAOReturnPOJOIDIfExistsInTable {

    long returnID(String name) throws SQLException;
}
