package example.sportal.model.dao;

import example.sportal.model.dao.interfaceDAO.IDAODeleteById;
import example.sportal.model.pojo.Article;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleDAO extends DAO implements IDAODeleteById {

    private static final String INSERT_ARTICLE =
            "INSERT INTO articles (title ,full_text, date_published, views, author_id) VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE_VIEWS = "UPDATE articles SET views = views + 1 WHERE id = ?;";
    private static final String SEARCH_ARTICLE_BY_TITLE =
            "SELECT id, title, full_text, date_published, views, author_id " +
                    "FROM articles " +
                    "WHERE title = ?;";
    private static final String FIND_ARTICLE_BY_TITLE_OR_CATEGORY =
            "SELECT a.id, a.title, a.date_published " +
                    "FROM articles AS a " +
                    "JOIN articles_categories AS aa ON a.id = aa.article_id " +
                    "JOIN categories AS c ON aa.category_id = c.id " +
                    "WHERE c.category_name LIKE ? " +
                    "UNION " +
                    "SELECT a.title " +
                    "FROM articles AS a " +
                    "WHERE a.title LIKE ?;";
    private static final String TOP_FIVE_MOST_VIEWED_ARTICLE =
            "SELECT id, title, date_published " +
                    "FROM articles " +
                    "WHERE DATE(date_published) = CURRENT_DATE() " +
                    "ORDER BY views DESC LIMIT 5;";
    private static final String DELETE_SQL = "DELETE FROM articles WHERE id = ?;";
    private static final String UPDATE_ARTICLE = "UPDATE articles SET title = ?, full_text = ? WHERE id = ?;";

    public int addArticle(Article article) throws SQLException {
        try (
                Connection connection = this.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement psForArticle = connection.prepareStatement(INSERT_ARTICLE, Statement.RETURN_GENERATED_KEYS)
        ) {
            psForArticle.setString(1, article.getTitle());
            psForArticle.setString(2, article.getFullText());
            Timestamp createDateAndTime = Timestamp.valueOf(LocalDateTime.now());
            psForArticle.setTimestamp(3, createDateAndTime);
            article.setCreateDateAndTime(createDateAndTime);
            psForArticle.setInt(4, 0);
            psForArticle.setLong(5, article.getAuthorId());
            int rowAffected = psForArticle.executeUpdate();
            ResultSet resultSet = psForArticle.getGeneratedKeys();
            resultSet.next();
            article.setId(resultSet.getLong(1));

            connection.setAutoCommit(false);
            // vasko : not finished yet
//            for (Category category : categoryList) {
//                statement.setLong(2, articleId);
//                statement.setLong(1, category.getId());
//                statement.executeUpdate();
//            }
//
//            for (Picture p : pictures) {
//                statement.setLong(2, articleId);
//                statement.setLong(1, p.getId());
//                statement.executeUpdate();
//            }

            connection.commit();
            return rowAffected;
        }
    }

    public void addViewOfSpecificArticleID(long articleID) throws SQLException {
        this.jdbcTemplate.update(UPDATE_VIEWS, articleID);
    }

    public Article articleByTitle(String title) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(SEARCH_ARTICLE_BY_TITLE, title);
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

    public List<Article> allArticlesByTitleOrCategory(String titleOrCategory) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_ARTICLE_BY_TITLE_OR_CATEGORY,
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

    public List<Article> topFiveMostViewedArticlesForToday() throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(TOP_FIVE_MOST_VIEWED_ARTICLE);
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

    @Override
    public int deleteById(long id) throws SQLException {
        this.setFKFalse();
        int rowAffected = this.jdbcTemplate.update(DELETE_SQL, id);
        this.setFKTrue();
        return rowAffected;
    }

    public int edit(Article article) {
        return this.jdbcTemplate.update(UPDATE_ARTICLE, article.getTitle(), article.getFullText(), article.getId());
    }
}
