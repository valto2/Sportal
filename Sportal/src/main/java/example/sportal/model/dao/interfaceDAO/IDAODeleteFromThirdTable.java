package example.sportal.model.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAODeleteFromThirdTable {

   void deleteFromThirdTable(long leftColumn, long rightColumn) throws SQLException;
}
