package DAO;

import elements.Article;
import elements.User;

import java.sql.SQLException;

public class UsersDislikeArticlesDAO {

    private static UsersDislikeArticlesDAO instance = new UsersDislikeArticlesDAO();

    private UsersDislikeArticlesDAO() {
    }

    public static UsersDislikeArticlesDAO getInstance() {
        return instance;
    }

    // dislike article TODO ?????
    public void dislikeOfSpecificArticle(Article article, User user) throws SQLException {

    }
}
