package example.sportal.dao;

import example.sportal.dao.interfaceDAO.IDAOAllInfo;
import example.sportal.dao.interfaceDAO.IDAODeleteByID;
import example.sportal.model.Article;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ArticleDAO extends DAO implements IDAODeleteByID, IDAOAllInfo {

    public void addArticle(Article article) throws SQLException {
        String insertArticleSQL =
                "INSERT INTO articles (title ,full_text, date_published, views, author_id) VALUES (?, ?, ?, ?, ?);";
        Timestamp createDateAndTime = Timestamp.valueOf(LocalDateTime.now());
        this.jdbcTemplate.update(
                insertArticleSQL,
                article.getTitle(), article.getFullText(), createDateAndTime, 0, article.getAuthorID());
    }

    public void addViewOfSpecificArticleID(long articleID) throws SQLException {
        String updateViewsSQL = "UPDATE articles SET views = views + 1 WHERE id = ?;";
        this.jdbcTemplate.update(updateViewsSQL, articleID);
    }

    public Article articleByTitle(String title) throws SQLException {
        String searchSQL =
                "SELECT a.id, a.title, a.full_text, a.date_published, a.views, a.author_id, u.user_name " +
                        "FROM articles AS a " +
                        "LEFT JOIN users AS u ON a.author_id = u.id " +
                        "WHERE a.title = ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(searchSQL, title);
        if (rowSet.next()) {
            return this.rowSetCreateArticle(rowSet);
        }
        return null;
    }

    private Article rowSetCreateArticle(SqlRowSet rowSet) {
        Article article = new Article();
        article.setId(rowSet.getLong("id"));
        article.setTitle(rowSet.getString("title"));
        article.setFullText(rowSet.getString("full_text"));
        article.setCreateDateAndTime(rowSet.getTimestamp("date_published"));
        article.setViews(rowSet.getInt("views") + 1);
        article.setAuthorID(rowSet.getLong("author_id"));
        if (rowSet.getString("user_name") != null) {
            article.setAuthorName(rowSet.getString("user_name"));
        }
        return article;
    }

    public List<String> allArticleByTitleOrCategory(String titleOrCategory) throws SQLException {
        String findAllTitleOfArticleSQL =
                "SELECT a.title " +
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
        List<String> listWithTitleOfArticles = new ArrayList();
        while (rowSet.next()) {
            listWithTitleOfArticles.add(rowSet.getString("title"));
        }
        return listWithTitleOfArticles;
    }

    public List<Article> topFiveMostViewedArticlesForToday() throws SQLException {
        String topFiveMostViewedArticleSQL =
                "SELECT a.id, a.title, a.full_text, a.date_published, a.views, a.author_id, u.user_name " +
                        "FROM articles AS a " +
                        "LEFT JOIN users AS u ON a.author_id = u.id " +
                        "WHERE DATE(a.date_published) = CURRENT_DATE() " +
                        "ORDER BY a.views DESC LIMIT 5;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(topFiveMostViewedArticleSQL);
        List<Article> listWithArticles = new ArrayList<>();
        while (rowSet.next()) {
            listWithArticles.add(this.rowSetCreateArticle(rowSet));
        }
        return listWithArticles;
    }

    public List<Article> allArticlesToASpecificCategory(String categoryName) throws SQLException {
        String allArticles =
                "SELECT a.id, a.title, a.full_text, a.date_published, a.views, a.author_id, u.user_name " +
                        "FROM articles AS a " +
                        "LEFT JOIN users AS u ON a.author_id = u.id " +
                        "JOIN articles_categories AS aa ON a.id = aa.article_id " +
                        "JOIN categories AS c ON aa.category_id = c.id " +
                        "WHERE c.category_name = ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(allArticles, categoryName);
        List<Article> listWithArticles = new ArrayList<>();
        while (rowSet.next()) {
            listWithArticles.add(this.rowSetCreateArticle(rowSet));
        }
        return listWithArticles;
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
    public int deleteByID(long id) throws SQLException {
        this.setFKFalse();
        String deleteSQL = "DELETE FROM articles WHERE id = ?;";
        int rowAffected = this.jdbcTemplate.update(deleteSQL, id);
        this.setFKTrue();
        return rowAffected;
    }

    public Article articleByID(long articleID) {
        String searchSQL = "SELECT a.id, a.title, a.full_text, a.date_published, a.views, a.author_id,  u.user_name " +
                "FROM articles AS a " +
                "LEFT JOIN users AS u ON a.author_id = u.id " +
                "WHERE a.id = ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(searchSQL, articleID);
        if (rowSet.next()) {
            return this.rowSetCreateArticle(rowSet);
        }
        return null;
    }

    @Override
    public Collection<Object> all() throws SQLException {
        String findAllTitleOfArticleSQL = "SELECT title FROM articles;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(findAllTitleOfArticleSQL);
        Collection<Object> listWithTitle = new ArrayList();
        while (rowSet.next()) {
            listWithTitle.add(rowSet.getString("title"));
        }
        return listWithTitle;
    }
}
