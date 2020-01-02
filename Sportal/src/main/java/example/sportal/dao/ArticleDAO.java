package example.sportal.dao;

import example.sportal.model.Article;
import example.sportal.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ArticleDAO {

    private static ArticleDAO instance = new ArticleDAO();

    private ArticleDAO() {
    }

    public static ArticleDAO getInstance() {
        return instance;
    }

    public void addArticleFromAdmin(Article article, User user) throws SQLException {
        if (user.isAdmin()) {
            Connection connection = DBManager.INSTANCE.getConnection();

            String insertArticleSQL = "INSERT INTO articles (title ,full_text, date_published, views, author_id) " +
                    "VALUES (?, ?, ?, ?, ?);";
            try (PreparedStatement statement = connection.prepareStatement(insertArticleSQL)) {
                statement.setString(1, article.getTitle());
                statement.setString(2, article.getFullText());
                statement.setTimestamp(3, article.getCreateDateAndTime());
                statement.setInt(4, 0);
                statement.setInt(5, article.getAuthorID());
                statement.executeUpdate();
                String success = "Successful added article!";
                System.out.println(success);
            }
        } else {
            String message = "You are not admin!";
            System.out.println(message);
        }
    }

    public void editTheTitleOfSpecificArticle(int articleID, User user, String newTitle) throws SQLException {
        if (user.isAdmin()) {
            Connection connection = DBManager.INSTANCE.getConnection();

            String updateArticleTitleSQL = "UPDATE articles SET title = ? WHERE id = ?;";
            try (PreparedStatement statement = connection.prepareStatement(updateArticleTitleSQL)) {
                statement.setInt(2, articleID);
                statement.setString(1, newTitle);
                int rowAffected = statement.executeUpdate();
                String success = rowAffected + " row, successful edited title of the article!";
                System.out.println(success);
            }
        } else {
            String message = "You are not admin or article not exists!";
            System.out.println(message);
        }
    }

    public void editTheTextOfSpecificArticle(int articleID, User user, String newText) throws SQLException {
        if (user.isAdmin()) {
            Connection connection = DBManager.INSTANCE.getConnection();

            String updateArticleTextSQL = "UPDATE articles SET full_text = ? WHERE id = ?;";
            try (PreparedStatement statement = connection.prepareStatement(updateArticleTextSQL)) {
                statement.setInt(2, articleID);
                statement.setString(1, newText);
                int rowAffected = statement.executeUpdate();
                String affected = rowAffected + " row, successful edited text of the article!";
                System.out.println(affected);
            }
        } else {
            String message = "You are not admin!";
            System.out.println(message);
        }
    }

    //
    public void deleteArticle(int articleID, User user) throws SQLException {
        if (user.isAdmin()) {
            Connection connection = DBManager.INSTANCE.getConnection();
            String deleteCommentSQL = "DELETE FROM articles WHERE id = ?;";
            try (PreparedStatement statement = connection.prepareStatement(deleteCommentSQL)) {
                statement.setInt(1, articleID);
                int rowAffected = statement.executeUpdate();
                String success = rowAffected + " row, successful delete article!";
                System.out.println(success);
            }
        } else {
            String message = "You are not admin or article not exists!";
            System.out.println(message);
        }
    }

    public void addViewOfSpecificArticle(int articleID) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();

        String updateViewsSQL = "UPDATE articles SET views = views + 1 WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(updateViewsSQL)) {
            statement.setInt(1, articleID);
            int rowAffected = statement.executeUpdate();
            String success = rowAffected + " row, successfully added view of article!";
            System.out.println(success);
        }
    }

    public ArrayList<Article> allArticleByTitleOrCategory(String titleOrCategory) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();

        String findAllArticleSQL = "SELECT a.id, a.title, a.full_text, a.date_published, a.views, a.author_id " +
                "FROM articles AS a " +
                "JOIN articles_categories AS aa ON a.id = aa.article_id " +
                "JOIN categories AS c ON aa.category_id = c.id " +
                "WHERE c.category_name LIKE ? " +
                "UNION " +
                "SELECT a.id, a.title, a.full_text, a.date_published, a.views, a.author_id " +
                "FROM articles AS a " +
                "WHERE a.title LIKE ?;";
        ArrayList<Article> listWithArticles = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(findAllArticleSQL)) {
            statement.setString(1, titleOrCategory + "%");
            statement.setString(2, titleOrCategory + "%");
            ResultSet row = statement.executeQuery();
            while (row.next()) {
                Article article = new Article();
                article.setId(row.getInt(1));
                article.setTitle(row.getString(2));
                article.setFullText(row.getString(3));
                article.setCreateDateAndTime(row.getTimestamp(4));
                article.setViews(row.getInt(5));
                article.setAuthorID(row.getInt(6));

                listWithArticles.add(article);
            }
        }
        return listWithArticles;
    }

    public String theAuthorNameOfSpecificArticle(int articleID) throws SQLException {
        String authorName = null;
        Connection connection = DBManager.INSTANCE.getConnection();

        String findAuthorByArticleIdSQL = "SELECT u.user_name " +
                "FROM users AS u " +
                "JOIN  articles AS a ON u.id = a.author_id " +
                "WHERE a.id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(findAuthorByArticleIdSQL)) {
            statement.setInt(1, articleID);
            ResultSet row = statement.executeQuery();
            if (row.next()) {
                authorName = row.getString("u.user_name");
                System.out.println("Successfully found of author!");
            }

            return authorName;
        }
    }

    public ArrayList<Article> topFiveMostViewedArticlesForToday() throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();

        String topFiveMostViewedArticleSQL = "SELECT a.id, a.title, a.full_text, a.date_published, a.views, a.author_id " +
                "FROM articles AS a " +
                "WHERE DATE(a.date_published) = CURRENT_DATE() " +
                "ORDER BY a.views DESC LIMIT 5;";
        ArrayList<Article> listWithArticles = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(topFiveMostViewedArticleSQL)) {
            ResultSet row = statement.executeQuery();
            while (row.next()) {
                Article article = new Article();
                article.setId(row.getInt("a.id"));
                article.setTitle(row.getString("a.title"));
                article.setFullText(row.getString("a.full_text"));
                article.setCreateDateAndTime(row.getTimestamp("a.date_published"));
                article.setViews(row.getInt("a.views"));
                article.setAuthorID(row.getInt("a.author_id"));

                listWithArticles.add(article);
            }
        }
        return listWithArticles;
    }
}
