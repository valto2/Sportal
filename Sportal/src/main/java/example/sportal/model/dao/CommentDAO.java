package example.sportal.model.dao;

import example.sportal.model.pojo.Article;
import example.sportal.model.pojo.Comment;
import example.sportal.model.pojo.User;
import example.sportal.exceptions.CommentException;

import java.sql.*;

public class CommentDAO {

    private static CommentDAO instance = new CommentDAO();

    public static CommentDAO getInstance() {
        return instance;
    }

    private CommentDAO(){}

    public void addCommentToArticle(Article article, Comment comment) throws CommentException {
        try {
            Connection connection = DBManager.INSTANCE.getConnection();
            String sql = "insert into comments " +
                    "(text, time_posted, owner_id, article_id) values" +
                    "(?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, comment.getFullCommentText());
            preparedStatement.setTimestamp(2, comment.getTimePosted());
            comment.setArticleID(article.getId());
            preparedStatement.setLong(3, comment.getArticleID());
            preparedStatement.setLong(4,  comment.getUserID());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int comment_id = resultSet.getInt(1);
            comment.setId(comment_id);
        }catch (SQLException e) {
            throw new CommentException("Failed to add comment. Please try again later.", e);
        }
    }

    public void addReplyToComment(Comment parentComment, Comment comment) throws CommentException{
        try {
            Connection connection = DBManager.INSTANCE.getConnection();
            String sql = "insert into comments " +
                    "(text, time_posted, article_id, user_id, replied_to_id) values" +
                    "(?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, comment.getFullCommentText());
            preparedStatement.setTimestamp(2, comment.getTimePosted());
            comment.setArticleID(parentComment.getArticleID());
            preparedStatement.setLong(3, comment.getArticleID());
            preparedStatement.setLong(4,  comment.getUserID());
            comment.setReply_id(parentComment.getId());
            preparedStatement.setLong(5,  comment.getReply_id());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int comment_id = resultSet.getInt(1);
            comment.setId(comment_id);
        } catch (SQLException e) {
            throw  new CommentException("Failed to reply on comment. Please, try again later.", e);
        }
    }

    public void editComment(String editedText, Comment comment) throws CommentException {
        try {
            Connection connection = DBManager.INSTANCE.getConnection();
            comment.setFullCommentText(editedText);
            String sql = "update comments set full_comment_text = ? where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, comment.getFullCommentText());
            preparedStatement.setLong(2, comment.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("Failed to edit comment. Please, try again later.", e);
        }
    }

    public void deleteComment(Comment comment) throws CommentException {
        try{
            Connection connection = DBManager.INSTANCE.getConnection();
            String deleteFromComments = "delete from comments where id = ? or reply_id = ?;";
            String deleteFromLikes = "delete from users_liked_comments where comment_id = ?";
            String deleteFromDislikes = "delete from users_disliked_comments where comment_id = ?";

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

            }catch (SQLException e) {
                connection.rollback();
                throw new CommentException("Failed to delete comment.", e);
            }
            finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new CommentException("Failed to delete comment", e);
        }
    }

    public void likeComment(User user, Comment comment) throws CommentException {
        try {
            Connection connection = DBManager.INSTANCE.getConnection();
            if(commentIsLiked(user, comment)){
                setForeignKeysToZero(connection);
                String unlike = "delete from users_liked_comments where user_id = ? and comment_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(unlike);
                preparedStatement.setLong(1, user.getId());
                preparedStatement.setLong(2, comment.getId());
                preparedStatement.executeUpdate();
                setForeignKeysToOne(connection);
            }
            else {
                if(commentIsDisliked(user, comment)){
                    setForeignKeysToZero(connection);
                    String sql = "delete from users_disliked_comments where user_id = ? and comment_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setLong(1, user.getId());
                    preparedStatement.setLong(2, comment.getId());
                    preparedStatement.executeUpdate();
                    setForeignKeysToOne(connection);
                }
                String like = "insert into users_liked_comments values (? , ?);";
                PreparedStatement preparedStatement = connection.prepareStatement(like);
                preparedStatement.setLong(1, user.getId());
                preparedStatement.setLong(2, comment.getId());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new CommentException("Failed to like comment. Please, try again later.",e);
        }
    }

    public void dislikeComment(User user, Comment comment) throws CommentException {
        try {
            Connection connection = DBManager.INSTANCE.getConnection();
            if(commentIsDisliked(user, comment)){
                setForeignKeysToZero(connection);
                String sql = "delete from users_disliked_comments where user_id = ? and comment_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, user.getId());
                preparedStatement.setLong(2, comment.getId());
                preparedStatement.executeUpdate();
                setForeignKeysToOne(connection);
            }
            else {
                if(commentIsLiked(user, comment)){
                    setForeignKeysToZero(connection);
                    String unlike = "delete from users_liked_comments where user_id = ? and comment_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(unlike);
                    preparedStatement.setLong(1, user.getId());
                    preparedStatement.setLong(2, comment.getId());
                    preparedStatement.executeUpdate();
                    setForeignKeysToOne(connection);
                }
                String dislike = "insert into users_disliked_comments values (? , ?);";
                PreparedStatement preparedStatement = connection.prepareStatement(dislike);
                preparedStatement.setLong(1, user.getId());
                preparedStatement.setLong(2, comment.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new CommentException("Failed to dislike comment. Please, try again later.",e);
        }
    }

    //unlocks the table to delete constrained rows
    private void setForeignKeysToZero(Connection connection) throws SQLException {
        String setFKChecksToZero = "set FOREIGN_KEY_CHECKS = 0;";
        PreparedStatement preparedStatementFKZero = connection.prepareStatement(setFKChecksToZero);
        preparedStatementFKZero.executeUpdate();
    }

    //locks the table to delete constrained rows
    private void setForeignKeysToOne(Connection connection) throws SQLException {
        String setFKChecksToOne = "set FOREIGN_KEY_CHECKS = 1;";
        PreparedStatement preparedStatementFKOne = connection.prepareStatement(setFKChecksToOne);
        preparedStatementFKOne.executeUpdate();
    }

    //finds if the current comment is already liked
    private boolean commentIsLiked(User user, Comment comment) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String sql = "select * from users_liked_comments where user_id = ? and comment_id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, comment.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    //finds if the current comment is already disliked
    private boolean commentIsDisliked(User user, Comment comment) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String sql = "select * from users_disliked_comments where user_id = ? and comment_id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, comment.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }


}