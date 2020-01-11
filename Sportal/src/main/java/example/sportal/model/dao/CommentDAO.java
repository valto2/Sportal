package example.sportal.model.dao;

import example.sportal.model.pojo.Article;
import example.sportal.model.pojo.Comment;
import example.sportal.model.pojo.User;
import example.sportal.exceptions.CommentException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.SneakyThrows;

import java.sql.*;

@Component
public class CommentDAO {

    private static final String INSERT_LIKE_COMMENT_SQL = "INSERT INTO users_like_comments VALUES (? , ?);";
    private static final String INSERT_DISLIKE_COMMENT_SQL = "INSERT INTO users_disliked_comments VALUES (? , ?);";
    private static final String DISLIKED_COMMENTS_SQL = "SELECT * FROM users_disliked_comments WHERE user_id = ? AND comment_id = ?;";
    private static final String LIKED_COMMENTS_SQL = "SELECT * FROM users_like_comments WHERE user_id = ? AND comment_id = ?;";
    private static final String UPDATE_COMMENT_SQL = "UPDATE comments SET full_comment_text = ? WHERE id = ?";
    private static final String DELETE_DISLIKED_COMMENTS_SQL = "DELETE FROM users_disliked_comments WHERE user_id = ? AND comment_id = ?";
    private static final String DELETE_LIKED_COMMENTS_SQL = "DELETE FROM users_like_comments WHERE user_id = ? AND comment_id = ?";
    private static final String INSTERT_COMMENT_SQL = "INSERT INTO comments " +
            "(full_comment_text, date_published, user_id, article_id) VALUES" + "(?,?,?,?);";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CommentDAO() {
    }

    public void addComment(Article article, Comment comment) throws CommentException {
        try {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSTERT_COMMENT_SQL, Statement.RETURN_GENERATED_KEYS);
            Timestamp timestamp = Timestamp.valueOf(comment.getTimePosted());
            preparedStatement.setString(1, comment.getFullCommentText());
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.setLong(3, comment.getArticleID());
            preparedStatement.setLong(4, comment.getUserID());
            comment.setArticleID(article.getId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int comment_id = resultSet.getInt(1);
            comment.setId(comment_id);
        } catch (SQLException e) {
            throw new CommentException("Failed to add comment. Please try again later.", e);
        }
    }

    public void editComment(String editedText, Comment comment) throws CommentException {
        try {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            comment.setFullCommentText(editedText);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COMMENT_SQL);
            preparedStatement.setString(1, comment.getFullCommentText());
            preparedStatement.setLong(2, comment.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("Failed to edit comment. Please, try again later.", e);
        }
    }

    public void deleteComment(Comment comment) throws CommentException {
        try {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            String deleteFromComments = "DELETE FROM comments WHERE id = ?;";
            String deleteFromLikes = "DELETE FROM users_like_comments WHERE comment_id = ?";
            String deleteFromDislikes = "DELETE FROM users_disliked_comments WHERE comment_id = ?";

            try (PreparedStatement deleteFromCommentsStatement = connection.prepareStatement(deleteFromComments);
                 PreparedStatement deleteFromLikesStatement = connection.prepareStatement(deleteFromLikes);
                 PreparedStatement deleteFromDislikesStatement = connection.prepareStatement(deleteFromDislikes)) {

                connection.setAutoCommit(false);

                setForeignKeysToZero(connection);

                deleteFromCommentsStatement.setLong(1, comment.getId());
                deleteFromCommentsStatement.setLong(2, comment.getId());
                deleteFromCommentsStatement.executeUpdate();

                setForeignKeysToOne(connection);

                deleteFromDislikesStatement.setLong(1, comment.getId());
                deleteFromDislikesStatement.executeUpdate();

                deleteFromLikesStatement.setLong(1, comment.getId());
                deleteFromLikesStatement.executeUpdate();

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw new CommentException("Failed to delete comment.", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new CommentException("Failed to delete comment", e);
        }
    }

    @SneakyThrows
    public String dislikeComment(User user, Comment comment) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            //removes comment from disliked comments
            if (commentIsAlreadyDisliked(user, comment)) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DISLIKED_COMMENTS_SQL)) {
                    preparedStatement.setLong(1, user.getId());
                    preparedStatement.setLong(2, comment.getId());
                    preparedStatement.executeUpdate();
                }
                return "Removed dislike!";
            } else {
                //removes comment from liked comments
                try {
                    connection.setAutoCommit(false);
                    if (commentIsAlreadyLiked(user, comment)) {
                        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LIKED_COMMENTS_SQL)) {
                            preparedStatement.setLong(1, user.getId());
                            preparedStatement.setLong(2, comment.getId());
                            preparedStatement.executeUpdate();
                        }
                    }
                    //adds comment to disliked comments
                    try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DISLIKE_COMMENT_SQL)) {
                        preparedStatement.setLong(1, user.getId());
                        preparedStatement.setLong(2, comment.getId());
                        preparedStatement.executeUpdate();
                    }
                    connection.commit();
                    connection.setAutoCommit(true);
                    return "Comment disliked!";
                } catch (SQLException e) {
                    connection.rollback();
                    throw new SQLException("Connection rollback for disliking comment!", e);
                }
            }
        }
    }

    @SneakyThrows
    public String likeComment(User user, Comment comment) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            //removes comment from liked comments
            if (commentIsAlreadyLiked(user, comment)) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LIKED_COMMENTS_SQL)) {
                    preparedStatement.setLong(1, user.getId());
                    preparedStatement.setLong(2, comment.getId());
                    preparedStatement.executeUpdate();
                }
                return "Removed like!";
            } else {
                //removes comment from disliked comments
                try {
                    connection.setAutoCommit(false);
                    if (commentIsAlreadyDisliked(user, comment)) {
                        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DISLIKED_COMMENTS_SQL)) {
                            preparedStatement.setLong(1, user.getId());
                            preparedStatement.setLong(2, comment.getId());
                            preparedStatement.executeUpdate();
                        }
                    }
                    try (PreparedStatement statement = connection.prepareStatement(INSERT_LIKE_COMMENT_SQL)) {
                        statement.setLong(1, user.getId());
                        statement.setLong(2, comment.getId());
                        statement.executeUpdate();
                    }
                    connection.commit();
                    connection.setAutoCommit(true);
                    return "Comment liked!";
                } catch (SQLException e) {
                    connection.rollback();
                    throw new SQLException("Connection rollback for liking comment!", e);
                }
            }
        }
    }
    //finds if the current comment is already liked
    private boolean commentIsAlreadyLiked(User user, Comment comment) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(LIKED_COMMENTS_SQL)) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, comment.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }
    //finds if the current comment is already disliked
    private boolean commentIsAlreadyDisliked(User user, Comment comment) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DISLIKED_COMMENTS_SQL)) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, comment.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }
    //unlocks the table to delete constrained rows
    private void setForeignKeysToZero(Connection connection) throws SQLException {
        String setFKChecksToZero = "SET FOREIGN_KEY_CHECKS = 0;";
        PreparedStatement preparedStatementFKZero = connection.prepareStatement(setFKChecksToZero);
        preparedStatementFKZero.executeUpdate();
    }
    //locks the table to delete constrained rows
    private void setForeignKeysToOne(Connection connection) throws SQLException {
        String setFKChecksToOne = "SET FOREIGN_KEY_CHECKS = 1;";
        PreparedStatement preparedStatementFKOne = connection.prepareStatement(setFKChecksToOne);
        preparedStatementFKOne.executeUpdate();
    }
}