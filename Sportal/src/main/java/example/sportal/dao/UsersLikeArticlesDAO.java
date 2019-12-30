package example.sportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersLikeArticlesDAO {

    private static UsersLikeArticlesDAO instance = new UsersLikeArticlesDAO();

    private UsersLikeArticlesDAO() {
    }

    public static UsersLikeArticlesDAO getInstance() {
        return instance;
    }


    public void likeArticle(int articleID, int userID) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String insertLikeSQL = "INSERT INTO users_like_articles (article_id, user_id) VALUE (?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(insertLikeSQL)) {
            statement.setInt(1, articleID);
            statement.setInt(2, userID);
            statement.executeUpdate();
            String success = "Successfully added like of article!";
            System.out.println(success);
        }
    }

    public void deleteLikeArticle(int articleID, int userID) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String deleteLikeSQL = "DELETE FROM users_like_articles WHERE article_id = ? AND user_id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(deleteLikeSQL)) {
            statement.setInt(1, articleID);
            statement.setInt(2, userID);
            int rowAffected = statement.executeUpdate();
            String success = rowAffected + " row, successfully delete like of article!";
            System.out.println(success);
        }
    }

    public int allLikesForSpecificArticleIDByID(int articleID) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String selectLikesSQL = "SELECT COUNT(user_id) FROM users_like_articles WHERE article_id = ?;";
        int numberOfTheLikesOfAComment = 0;
        try (PreparedStatement statement = connection.prepareStatement(selectLikesSQL)) {
            statement.setInt(1, articleID);
            ResultSet row = statement.executeQuery();
            if (row.next()){
                numberOfTheLikesOfAComment = row.getInt(1);
            }
        }
        return numberOfTheLikesOfAComment;
    }
}
