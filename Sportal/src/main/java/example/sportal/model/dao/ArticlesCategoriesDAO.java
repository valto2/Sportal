package example.sportal.model.dao;

import example.sportal.model.dao.interfaceDAO.IDAODeleteFromThirdTable;
import example.sportal.model.dao.interfaceDAO.IDAOExistsInThirdTable;
import example.sportal.model.dao.interfaceDAO.IDAOManyToMany;
import example.sportal.model.pojo.Article;
import example.sportal.model.pojo.Category;
import example.sportal.model.pojo.POJO;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class ArticlesCategoriesDAO extends DAO
        implements IDAOManyToMany, IDAODeleteFromThirdTable, IDAOExistsInThirdTable {


    @Override
    public boolean addInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        String insertCategoryToArticleSQL = "INSERT INTO articles_categories (article_id ,category_id) VALUES (?,?);";
        int rowAffected = this.jdbcTemplate.update(insertCategoryToArticleSQL, leftColumn, rightColumn);
        if (rowAffected == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void deleteFromThirdTable(long leftColumn, long rightColumn) throws SQLException {
        String deleteDislikeSQL = "DELETE FROM articles_categories WHERE article_id = ? AND category_id = ?;";
        this.jdbcTemplate.update(deleteDislikeSQL, leftColumn, rightColumn);
    }

    public Collection<POJO> allArticlesByCategoryID(long categoryID) throws SQLException {
        String allCategories =
                "SELECT a.id, a.title " +
                        "FROM articles AS a " +
                        "JOIN articles_categories AS ac ON a.id = ac.article_id " +
                        "JOIN categories AS c ON c.id = ac.category_id " +
                        "WHERE category_id = ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(allCategories, categoryID);
        Collection<POJO> listWithCategories = new ArrayList<>();
        while (rowSet.next()) {
            listWithCategories.add(this.createArticleByRowSet(rowSet));
        }

        return listWithCategories;
    }

    private Article createArticleByRowSet(SqlRowSet rowSet) {
        Article article = new Article();
        article.setId(rowSet.getInt("id"));
        article.setTitle(rowSet.getString("title"));
        return article;
    }

    public Collection<POJO> allCategoriesByArticlesID(long articleID) throws SQLException {
        String allCategories =
                "SELECT c.id, c.category_name " +
                        "FROM categories AS c " +
                        "JOIN articles_categories AS ac ON c.id = ac.category_id " +
                        "JOIN articles AS a ON a.id = ac.article_id " +
                        "WHERE article_id = ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(allCategories, articleID);
        Collection<POJO> listWithCategories = new ArrayList<>();
        while (rowSet.next()) {
            listWithCategories.add(this.createCategoryByRowSet(rowSet));
        }
        return listWithCategories;
    }

    private Category createCategoryByRowSet(SqlRowSet rowSet) {
        Category category = new Category();
        category.setId(rowSet.getInt("id"));
        category.setCategoryName(rowSet.getString("category_name"));
        return category;
    }

    @Override
    public boolean existsInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        String selectDislikesSQL =
                "SELECT article_id, category_id " +
                        "FROM articles_categories " +
                        "WHERE article_id = ? AND category_id = ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(selectDislikesSQL, leftColumn, rightColumn);
        return rowSet.next();
    }
}
