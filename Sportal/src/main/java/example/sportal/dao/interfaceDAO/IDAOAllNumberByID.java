package example.sportal.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAOAllNumberByID {

    int allByID(long id) throws SQLException;
}
