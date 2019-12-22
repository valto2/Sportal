package DAO;

import elements.Author;
import model.article.Article;
import model.db.DBManager;

import java.sql.*;

public class AuthorDAO {

    private static AuthorDAO instance = new AuthorDAO();

    private AuthorDAO() {
    }

    public static AuthorDAO getInstance() {
        return instance;
    }

    public void addingAuthor(Author author) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();

        String insertAuthorSQL =
                "INSERT INTO authors (first_name, last_name) " +
                        "VALUES (?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(insertAuthorSQL)) {
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());
            statement.executeUpdate();

            System.out.println("Successful added of author!");
        }
    }

    public Author theAuthorOfSpecificArticle(Article article) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();

        String findAuthorByArticleId = "SELECT " +
                "a.id, " +
                "a.first_name, " +
                "a.last_name , " +
                "FROM authors AS a " +
                "JOIN  articles AS ar ON a.id = ar.author_id " +
                "WHERE a.article_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(findAuthorByArticleId)) {

            statement.setInt(1, article.getAuthorID());

            ResultSet row = statement.executeQuery();
            Author author = null;
            if (row.next()) {
                author.setId(row.getInt("a.id"));
                author.setFirstName(row.getString("a.first_name"));
                author.setLastName(row.getString("a.last_name"));
                System.out.println("Successful find of author!");
            }

            return author;
        }
    }
}
