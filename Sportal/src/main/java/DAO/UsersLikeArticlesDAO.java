package DAO;

import model.db.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
