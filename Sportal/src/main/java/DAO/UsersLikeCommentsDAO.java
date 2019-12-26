package DAO;

import model.db.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsersLikeCommentsDAO {

    private static UsersLikeCommentsDAO instance = new UsersLikeCommentsDAO();

    private UsersLikeCommentsDAO() {
    }

    public static UsersLikeCommentsDAO getInstance() {
        return instance;
    }

    public void likeComment(int commentID, int userID) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String insertLikeSQL = "INSERT INTO users_like_comments (comment_id, user_id) VALUE (?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(insertLikeSQL)) {
            statement.setInt(1, commentID);
            statement.setInt(2, userID);
            statement.executeUpdate();
            String success = "Successfully added like of comment!";
            System.out.println(success);
        }
    }
}
