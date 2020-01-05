package example.sportal.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAOManyToMany {

    boolean addInThirdTable(long leftColumn, long rightColumn) throws SQLException;
}
