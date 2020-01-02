package example.sportal.dao;

import example.sportal.dao.interfaceDAO.IDAOAllNumberByID;
import example.sportal.dao.interfaceDAO.IDAODeleteFromThirdTable;
import example.sportal.dao.interfaceDAO.IDAOManyToMany;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class UsersLikeArticlesDAO extends DAO implements IDAOManyToMany, IDAODeleteFromThirdTable, IDAOAllNumberByID {

    @Override
    public void addInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        String insertSQL = "INSERT INTO users_like_articles (article_id, user_id) VALUE (?, ?);";
        this.jdbcTemplate.update(insertSQL,leftColumn,rightColumn);
    }

    @Override
    public void deleteFromThirdTable(long leftColumn, long rightColumn) throws SQLException {
        String deleteSQL = "DELETE FROM users_like_articles WHERE article_id = ? AND user_id = ?;";
        this.jdbcTemplate.update(deleteSQL,leftColumn,rightColumn);
    }

    @Override
    public int allByID(long id) throws SQLException {
        String countLikesSQL = "SELECT COUNT(user_id) AS number_likes FROM users_like_articles WHERE article_id = ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(countLikesSQL, id);
        if (rowSet.next()) {
            return rowSet.getInt("number_likes");
        }
        return 0;
    }
}
