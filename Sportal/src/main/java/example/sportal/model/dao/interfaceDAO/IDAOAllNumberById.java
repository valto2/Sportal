package example.sportal.model.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAOAllNumberById {

    int allById(long id) throws SQLException;
}
