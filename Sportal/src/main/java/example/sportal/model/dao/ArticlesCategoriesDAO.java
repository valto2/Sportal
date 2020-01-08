package example.sportal.model.dao;

import example.sportal.exceptions.TransactionException;
import example.sportal.model.dao.interfaceDAO.IDAODeleteFromThirdTable;
import example.sportal.model.dao.interfaceDAO.IDAOExistsInThirdTable;
import example.sportal.model.pojo.Article;
import example.sportal.model.pojo.Category;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class ArticlesCategoriesDAO extends DAO implements IDAODeleteFromThirdTable, IDAOExistsInThirdTable {


    private static final String INSERT_CATEGORIES_TO_ARTICLE =
            "INSERT INTO articles_categories (category_id, article_id) VALUES (?, ?);";
    private static final String DELETE_FROM_ARTICLES_CATEGORIES_TABLE =
            "DELETE FROM articles_categories WHERE article_id = ? AND category_id = ?;";
    private static final String ALL_TITLE_OF_ARTICLE_BY_CATEGORY_ID =
            "SELECT a.id, a.title " +
                    "FROM articles AS a " +
                    "JOIN articles_categories AS ac ON a.id = ac.article_id " +
                    "JOIN categories AS c ON c.id = ac.category_id " +
                    "WHERE category_id = ?;";
    private static final String ALL_CATEGORIES_BY_ARTICLE_ID =
            "SELECT c.id, c.category_name " +
                    "FROM categories AS c " +
                    "JOIN articles_categories AS ac ON c.id = ac.category_id " +
                    "JOIN articles AS a ON a.id = ac.article_id " +
                    "WHERE article_id = ?;";
    private static final String CHECK_EXISTS =
            "SELECT article_id, category_id " +
                    "FROM articles_categories " +
                    "WHERE article_id = ? AND category_id = ?;";

    public void addListFromCategoriesToArticleId(Collection<Category> categoryList, long articleId) throws SQLException {
        Connection connection = this.jdbcTemplate.getDataSource().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_CATEGORIES_TO_ARTICLE)) {
            // vasko : not finished yet
        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new TransactionException("Unsuccessful connection commit for add categories! " + e.getMessage());
            } catch (SQLException ex) {
                throw new SQLException("Unsuccessful connection rollback in add categories method! " + ex.getMessage());
            }
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void deleteFromThirdTable(long leftColumn, long rightColumn) throws SQLException {
        this.jdbcTemplate.update(DELETE_FROM_ARTICLES_CATEGORIES_TABLE, leftColumn, rightColumn);
    }

    public Collection<Article> allArticlesByCategoryID(long categoryID) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ALL_TITLE_OF_ARTICLE_BY_CATEGORY_ID, categoryID);
        Collection<Article> listWithCategories = new ArrayList<>();
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

    public Collection<Category> allCategoriesByArticlesID(long articleID) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ALL_CATEGORIES_BY_ARTICLE_ID, articleID);
        Collection<Category> listWithCategories = new ArrayList<>();
        while (rowSet.next()) {
            listWithCategories.add(this.createCategoryByRowSet(rowSet));
        }
        return listWithCategories;
    }

    private Category createCategoryByRowSet(SqlRowSet rowSet) {
        Category category = new Category();
        category.setId(rowSet.getLong("id"));
        category.setCategoryName(rowSet.getString("category_name"));
        return category;
    }

    @Override
    public boolean existsInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(CHECK_EXISTS, leftColumn, rightColumn);
        return rowSet.next();
    }
}
