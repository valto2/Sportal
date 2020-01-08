package example.sportal.model.dao;

import example.sportal.model.dao.interfaceDAO.IDAOAllInfo;
import example.sportal.model.dao.interfaceDAO.IDAODeleteById;
import example.sportal.model.pojo.Article;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ArticleDAO extends DAO implements IDAODeleteById, IDAOAllInfo {

    private static final String INSERT_ARTICLE =
            "INSERT INTO articles (title ,full_text, date_published, views, author_id) VALUES (?, ?, ?, ?, ?);";

    public int addArticle(Article article) throws SQLException {
        Connection connection = this.jdbcTemplate.getDataSource().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(INSERT_ARTICLE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, article.getTitle());
            ps.setString(2, article.getFullText());
            Timestamp createDateAndTime = Timestamp.valueOf(LocalDateTime.now());
            ps.setTimestamp(3, createDateAndTime);
            article.setCreateDateAndTime(createDateAndTime);
            ps.setInt(4, 0);
            ps.setLong(5, article.getAuthorId());
            int rowAffected = ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            resultSet.next();
            article.setId(resultSet.getLong(1));
            return rowAffected;
        }
    }

    public void addViewOfSpecificArticleID(long articleID) throws SQLException {
        String updateViewsSQL = "UPDATE articles SET views = views + 1 WHERE id = ?;";
        this.jdbcTemplate.update(updateViewsSQL, articleID);
    }

    public Article articleByTitle(String title) throws SQLException {
        String searchSQL =
                "SELECT id, title, full_text, date_published, views, author_id " +
                        "FROM articles " +
                        "WHERE title = ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(searchSQL, title);
        if (rowSet.next()) {
            return this.createArticleByRowSet(rowSet);
        }
        return null;
    }

    private Article createArticleByRowSet(SqlRowSet rowSet) {
        Article article = new Article();
        article.setId(rowSet.getLong("id"));
        article.setTitle(rowSet.getString("title"));
        article.setFullText(rowSet.getString("full_text"));
        article.setCreateDateAndTime(rowSet.getTimestamp("date_published"));
        article.setViews(rowSet.getInt("views") + 1);
        article.setAuthorId(rowSet.getLong("author_id"));
        return article;
    }

    public List<Article> allArticleByTitleOrCategory(String titleOrCategory) throws SQLException {
        String findAllTitleOfArticleSQL =
                "SELECT a.id, a.title, a.date_published " +
                        "FROM articles AS a " +
                        "JOIN articles_categories AS aa ON a.id = aa.article_id " +
                        "JOIN categories AS c ON aa.category_id = c.id " +
                        "WHERE c.category_name LIKE ? " +
                        "UNION " +
                        "SELECT a.title " +
                        "FROM articles AS a " +
                        "WHERE a.title LIKE ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(findAllTitleOfArticleSQL,
                titleOrCategory + "%", titleOrCategory + "%");
        List<Article> listFromArticles = new ArrayList<>();
        while (rowSet.next()) {
            Article article = new Article();
            article.setId(rowSet.getLong("id"));
            article.setTitle(rowSet.getString("title"));
            article.setCreateDateAndTime(rowSet.getTimestamp("date_published"));
            listFromArticles.add(article);
        }
        return listFromArticles;
    }

    public List<String> topFiveMostViewedArticlesForToday() throws SQLException {
        String topFiveMostViewedArticleSQL =
                "SELECT title " +
                        "FROM articles " +
                        "WHERE DATE(date_published) = CURRENT_DATE() " +
                        "ORDER BY views DESC LIMIT 5;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(topFiveMostViewedArticleSQL);
        List<String> listWithTitleOfArticles = new ArrayList<>();
        while (rowSet.next()) {
            listWithTitleOfArticles.add(rowSet.getString("title"));
        }
        return listWithTitleOfArticles;
    }

    public void editTheTitleOfSpecificArticle(int articleID, String newTitle) throws SQLException {
        String updateArticleTitleSQL = "UPDATE articles SET title = ? WHERE id = ?;";
        this.jdbcTemplate.update(updateArticleTitleSQL, newTitle, articleID);
    }

    public void editTheTextOfSpecificArticle(int articleID, String newText) throws SQLException {
        String updateArticleTextSQL = "UPDATE articles SET full_text = ? WHERE id = ?;";
        this.jdbcTemplate.update(updateArticleTextSQL, newText, articleID);
    }

    // todo delete article, delete and picture and categories to article
    @Override
    public int deleteById(long id) throws SQLException {
        this.setFKFalse();
        String deleteSQL = "DELETE FROM articles WHERE id = ?;";
        int rowAffected = this.jdbcTemplate.update(deleteSQL, id);
        this.setFKTrue();
        return rowAffected;
    }

    @Override
    public Collection<String> all() throws SQLException {
        String findAllTitleOfArticleSQL = "SELECT title FROM articles;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(findAllTitleOfArticleSQL);
        Collection<String> listWithTitle = new ArrayList<>();
        while (rowSet.next()) {
            listWithTitle.add(rowSet.getString("title"));
        }
        return listWithTitle;
    }
}
