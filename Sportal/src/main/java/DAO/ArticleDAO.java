package DAO;

import elements.Article;
import elements.User;
import model.db.DBManager;

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
        if (user.getAdmin()) {
            Connection connection = DBManager.INSTANCE.getConnection();

            String insertArticleSQL = "INSERT INTO articles (title ,full_text, date_published, views, author_id) " +
                    "VALUES (?, ?, ?, ?, ?);";
            try (PreparedStatement statement = connection.prepareStatement(insertArticleSQL)) {
                statement.setString(1, article.getTitle());
                statement.setString(2, article.getFullText());
                statement.setTimestamp(3, article.getCreateDateAndTime());
                statement.setInt(4, 0);
                statement.setInt(5, user.getId());
                statement.executeUpdate();
                String success = "Successful add article!";
                System.out.println(success);
            }
        } else {
            String message = "You are not admin!";
            System.out.println(message);
        }
    }

    // edit article
    // edit title (and last date of edited)
    // edit full_text (and last date of edited)

    public void addViewOfSpecificArticle(Article article) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();

        String updateViewsSQL = "UPDATE articles SET views = views + 1 WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(updateViewsSQL)) {
            statement.setInt(1, article.getId());
            statement.executeUpdate();
            String success = "Successfully added view of article!";
            System.out.println(success);
        }
    }

    public void likeOfSpecificArticle(Article article, User user) throws SQLException {
        if (!user.getAdmin()) {
            Connection connection = DBManager.INSTANCE.getConnection();

            String insertLikeSQL = "INSERT INTO users_like_articles (article_id, user_id) VALUE (?, ?);";

            try (PreparedStatement statement = connection.prepareStatement(insertLikeSQL)) {
                statement.setInt(1, article.getId());
                statement.setInt(2, user.getId());
                statement.executeUpdate();
                String success = "Successfully added like of article!";
                System.out.println(success);
            }
        } else {
            String youAreAdmin = "You are admin. You have not to like a article!";
            System.out.println(youAreAdmin);
        }
    }

    // dislike article TODO ?????
    public void dislikeOfSpecificArticle(Article article, User user) throws SQLException {
//        if (!user.getAdmin()) {
//            Connection connection = DBManager.INSTANCE.getConnection();
//
//            String insertLikeSQL = "INSERT INTO users_like_articles (article_id, user_id) VALUE (?, ?);";
//
//            try (PreparedStatement statement = connection.prepareStatement(insertLikeSQL)) {
//                statement.setInt(1, article.getId());
//                statement.setInt(2, user.getId());
//                statement.executeUpdate();
//                String success = "Successfully added like of article!";
//                System.out.println(success);
//            }
//        }else {
//            String youAreAdmin = "You are admin. You have not to like a article!";
//            System.out.println(youAreAdmin);
//        }
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

    public String theAuthorNameOfSpecificArticle(Article article) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();

        String findAuthorByArticleIdSQL = "SELECT u.user_name " +
                "FROM users AS u " +
                "JOIN  articles AS a ON u.id = a.author_id " +
                "WHERE a.id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(findAuthorByArticleIdSQL)) {

            statement.setInt(1, article.getAuthorID());

            ResultSet row = statement.executeQuery();
            String authorName = null;
            if (row.next()) {
                authorName = row.getString("u.user_name");
                System.out.println("Successful find of author!");
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
