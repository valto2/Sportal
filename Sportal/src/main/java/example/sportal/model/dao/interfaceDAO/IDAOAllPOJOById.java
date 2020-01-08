package example.sportal.model.dao.interfaceDAO;

import example.sportal.model.pojo.POJO;

import java.sql.SQLException;
import java.util.Collection;

public interface IDAOAllPOJOById {

    Collection<POJO> allById(long id) throws SQLException;
}
