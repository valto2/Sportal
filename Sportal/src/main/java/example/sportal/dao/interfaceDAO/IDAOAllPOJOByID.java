package example.sportal.dao.interfaceDAO;

import example.sportal.model.POJO;

import java.sql.SQLException;
import java.util.Collection;

public interface IDAOAllPOJOByID {

    Collection<POJO> allByID(long id) throws SQLException;
}
