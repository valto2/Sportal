package example.sportal.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAOReturnPOJOIDIfExistsInTable {

    long returnID(String name) throws SQLException;
}
