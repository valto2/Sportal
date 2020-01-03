package example.sportal.dao.interfaceDAO;

import java.sql.SQLException;
import java.util.Collection;

public interface IDAOAllInfo {

    Collection<Object> all()throws SQLException;
}
