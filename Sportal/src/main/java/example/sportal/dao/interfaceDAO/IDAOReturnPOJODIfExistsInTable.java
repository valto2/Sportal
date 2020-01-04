package example.sportal.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAOReturnPOJODIfExistsInTable {

    long returnID(String name) throws SQLException;
}
