package DAO;

import elements.Comment;
import elements.User;

import java.sql.SQLException;

public class UsersDislikeCommentsDAO {

    private static UsersDislikeCommentsDAO instance = new UsersDislikeCommentsDAO();

    private UsersDislikeCommentsDAO() {
    }

    public static UsersDislikeCommentsDAO getInstance() {
        return instance;
    }

    // todo dislike ?????
    public void dislikeComment(User user, Comment comment) throws SQLException {

    }
}
