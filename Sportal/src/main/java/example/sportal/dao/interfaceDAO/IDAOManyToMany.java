package example.sportal.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAOManyToMany {

    void addInThirdTable(long leftColumn, long rightColumn) throws SQLException;
}
