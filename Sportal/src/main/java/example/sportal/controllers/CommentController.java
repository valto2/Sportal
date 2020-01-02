//package example.sportal.controllers;
//
//import example.sportal.dao.DBManager;
//import example.sportal.exceptions.CommentException;
//import example.sportal.exceptions.PostException;
//import example.sportal.exceptions.UserException;
//import example.sportal.model.Article;
//import example.sportal.model.Comment;
//import example.sportal.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.sql.*;
//import java.util.HashSet;
//import java.util.TreeSet;
//
//public class CommentController {
//
//    @Component
//    public final class CommentDao extends AbstractDao { // used to operate with table 'comments' from db
//        @Autowired
//        UserDao userDao;
//        @Autowired
//        PostDao postDao;
//
//        // ::::::::: insert/remove from db :::::::::
//        public void insertComment(Comment comment, User sentBy) throws PostException, UserException {
//            try (Connection connection = DBManager.getInstance().getConnection();
//                 PreparedStatement ps = connection.prepareStatement(
//                    "insert into comments (content, post_id, user_id, date_time) values (?, ?, ?, now());",
//                    Statement.RETURN_GENERATED_KEYS);) {
//                ps.setString(1, comment.getContent());
//                ps.setLong(2, comment.getPostId());
//                ps.setLong(3, sentBy.getUserId());
//                ps.executeUpdate();
//                ResultSet rs = ps.getGeneratedKeys();
//                rs.next();
//                comment.setId(rs.getLong(1));
//                // !!! insert in post POJO comments collection required:
//                postDao.(postDao.getPostById(comment.getPostId()), comment);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public void deleteComment(Comment comment) throws SQLException, PostException, UserException {
//            try (Connection connection = DBManager.getInstance().getConnection();
//                 PreparedStatement ps =connection.prepareStatement(
//                    "delete from comments where id = ? and content = ? and post_id = ? and user_id = ? and date_time = ?;",
//                    Statement.RETURN_GENERATED_KEYS);) {
//                ps.setLong(1, comment.getId());
//                ps.setString(2, comment.getContent());
//                ps.setLong(3, comment.getPostId());
//                ps.setLong(4, comment.getUserId());
//                ps.setTimestamp(5, comment.getDatetime());
//                ps.executeUpdate();
//                ResultSet rs = ps.getGeneratedKeys();
//                rs.next();
//                comment.setId(rs.getLong(1));
//                // !!! delete from post POJO comments collection required:
//                postDao.deleteComment(postDao.getPostById(comment.getPostId()), comment);
//            }
//        }
//
//        public Comment getCommentById(long id) throws CommentException, SQLException, UserException, PostException {
//            Comment c = null;
//            try (Connection connection = DBManager.getInstance().getConnection();
//                 PreparedStatement ps =connection.prepareStatement(
//                    "select comment_id, content, likes_counter, dislikes_counter, post_id, user_id, date_time from comments where comment_id = ?;");) {
//                ps.setLong(1, id);
//                ResultSet rs = ps.executeQuery();
//                if (rs.next()) {
//                    User sentBy = userDao.getUserById(rs.getLong("user_id"));
//                    sentBy.setProfilePic(multimediaDao.getMultimediaById(sentBy.getProfilePic().getId()));
//                    c = new Comment(rs.getLong("comment_id"), rs.getString("content"), rs.getInt("likes_counter"),
//                            rs.getInt("dislikes_counter"), rs.getLong("post_id"), rs.getLong("user_id"),
//                            rs.getTimestamp("date_time"));
//                    c.setPeopleLiked(this.getAllPeopleLiked(c));
//                    c.setPeopleDisliked(this.getAllPeopleDisliked(c));
//                }
//                return c;
//            }
//        }
//
//        // ::::::::: loading comments for post :::::::::
//        public TreeSet<Comment> getCommentsForPost(Article article) throws SQLException, CommentException, UserException, PostException {
//            TreeSet<Comment> comments = new TreeSet<Comment>();
//            try (Connection connection = DBManager.getInstance().getConnection();
//                 PreparedStatement ps = connection.prepareStatement(
//                    "select comment_id, content, likes_counter, dislikes_counter, post_id, user_id, date_time from comments where post_id = ?;");) {
//                ps.setLong(1, article.getId());
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    User sentBy = userDao.getUserById(rs.getLong("user_id"));
//                    sentBy.setProfilePic(multimediaDao.getMultimediaById(sentBy.getProfilePic().getId()));
//                    Comment comment = new Comment(rs.getLong("comment_id"), rs.getString("content"), rs.getInt("likes_counter"),
//                            rs.getInt("dislikes_counter"), article.getId(), rs.getLong("user_id"),
//                            rs.getTimestamp("date_time"), sentBy);
//                    comment.setPeopleDisliked(this.getAllPeopleDisliked(comment));
//                    comment.setPeopleLiked(getAllPeopleLiked(comment));
//                    comments.add(comment);
//                }
//                return comments;
//            }
//        }
//
//        public HashSet<Long> getAllPeopleLiked(Comment comment) {
//            HashSet<Long> peopleLiked = new HashSet<>();
//            try {
//                Connection connection = DBManager.getInstance().getConnection();
//                PreparedStatement ps = connection.prepareStatement("SELECT user_id FROM comments_reactions WHERE comment_id=? AND reaction=1");
//                ps.setLong(1, comment.getId());
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    long currentId = rs.getLong("user_id");
//                    peopleLiked.add(currentId);
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            return peopleLiked;
//        }
//
//        public HashSet<Long> getAllPeopleDisliked(Comment comment) {
//            HashSet<Long> peopleDisliked = new HashSet<>();
//            try {
//                Connection connection = DBManager.getInstance().getConnection();
//                PreparedStatement ps = connection.prepareStatement("SELECT user_id FROM comments_reactions WHERE comment_id=? AND reaction=0");
//                ps.setLong(1, comment.getId());
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    long currentId = rs.getLong("user_id");
//                    peopleDisliked.add(currentId);
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            return peopleDisliked;
//        }
//
//
//        // ::::::::: like/dislike operations :::::::::
//        // currently comments are not keeping data for the users who liked/disliked them
//        public void incrementLikes(Comment comment) throws SQLException, CommentException {
//            Connection connection = DBManager.getInstance().getConnection();
//            try (PreparedStatement ps = connection
//                    .prepareStatement("update comments set likes_counter = ? where id = ?;")) {
//                ps.setInt(1, comment.getLikesCount());
//                ps.setLong(2, comment.getId());
//                comment.incrementLikes();
//            }
//        }
//
//        public void incrementDislikes(Comment comment) throws SQLException, CommentException {
//            Connection connection = DBManager.getInstance().getConnection();
//            try (PreparedStatement ps = connection
//                    .prepareStatement("update comments set dislikes_counter = ? where id = ?;");) {
//                ps.setInt(1, comment.getDislikesCount());
//                ps.setLong(2, comment.getId());
//                comment.incrementDislikes();
//            }
//        }
//
//        public boolean existsReaction(long commentId, long userId) {
//            try {
//                Connection connection = DBManager.getInstance().getConnection();
//                PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM comments_reactions WHERE comment_id = ? AND user_id=? ");
//                ps.setLong(1, commentId);
//                ps.setLong(2, userId);
//                ResultSet rs = ps.executeQuery();
//                rs.next();
//                if (rs.getInt("COUNT(*)") > 0) {
//                    return true;
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//
//        public void updateReaction(boolean reaction, long commentId, long userId) {
//            try {
//                Connection connection = DBManager.getInstance().getConnection();
//                PreparedStatement ps = connection.prepareStatement("UPDATE comments_reactions SET reaction=? WHERE comment_id=? AND user_id=? ");
//                ps.setBoolean(1, reaction);
//                ps.setLong(2, commentId);
//                ps.setLong(3, userId);
//                ps.executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public void insertReaction(boolean b, long commentId, long userId) {
//            try {
//                Connection connection = DBManager.getInstance().getConnection();
//                PreparedStatement ps = connection.prepareStatement("INSERT INTO comments_reactions(comment_id, reaction, user_id) VALUE (?,?,?)");
//                ps.setLong(1, commentId);
//                ps.setBoolean(2, b);
//                ps.setLong(3, userId);
//                ps.executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public void deleteReaction(long commentId, long userId) {
//            try {
//                Connection connection = DBManager.getInstance().getConnection();
//                PreparedStatement ps =connection.prepareStatement("DELETE FROM comments_reactions WHERE comment_id=? AND user_id=?");
//                ps.setLong(1, commentId);
//                ps.setLong(2, userId);
//                ps.executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
