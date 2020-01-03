package example.sportal.dao;

import example.sportal.dao.interfaceDAO.IDAODeleteFromThirdTable;
import example.sportal.dao.interfaceDAO.IDAOManyToMany;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class ArticlesCategoriesDAO extends DAO implements IDAOManyToMany, IDAODeleteFromThirdTable {


    @Override
    public void addInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        String insertCategoryToArticleSQL = "INSERT INTO articles_categories (article_id ,category_id) VALUES (?,?);";
        this.jdbcTemplate.update(insertCategoryToArticleSQL, leftColumn, rightColumn);
    }

    @Override
    public void deleteFromThirdTable(long leftColumn, long rightColumn) throws SQLException {
        String deleteDislikeSQL =
                        "DELETE FROM articles_categories " +
                        "WHERE article_id = ? AND category_id = ?;";
        this.jdbcTemplate.update(deleteDislikeSQL, leftColumn, rightColumn);
    }
}
