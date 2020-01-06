package example.sportal.model.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAODeleteById {

    int deleteById(long id) throws SQLException;

}
