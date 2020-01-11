package example.sportal.model.dao;

import example.sportal.model.pojo.Comment;
import example.sportal.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;


@Component
public class UserDAO {
    private static final String GET_COMMENTS_SQL = "SELECT id" + "FROM `technopolis`.orders" + "WHERE user_id = ?;"; // TODO: 10.1.2020 г.  
    private static final String IS_ADMIN_SQL = "SELECT is_admin FROM users WHERE id = ?;";
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE user_email = ?;";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?;";
    private static final String REGISTER_USER_SQL = "INSERT INTO users (" +
            "user_name, " +
            "user_password, " +
            "user_email, " +
            "VALUES (?,?,?);";

    private static final String SELECT_USER_BY_USERNAME = "SELECT " +
            "id, " +
            "user_name, " +
            "user_email, " +
            "user_password, " +
            "FROM users " +
            "WHERE user_name = ?;";

    private static final String EDIT_USER_SQL = "UPDATE users" +
            "SET " +
            "user_name = ?" +
            "user_password?" +
            "user_email = ?" +
            "WHERE id = ?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser(User user) throws SQLException {
        try ( Connection connection = jdbcTemplate.getDataSource().getConnection();
              PreparedStatement statement = connection.prepareStatement(REGISTER_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            user.setId(keys.getLong(1));
        }
    }

    public User deleteUser(User user) throws SQLException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_SQL)) {
            statement.setLong(1, user.getId());
            statement.execute();
        }
        return user;
    }

    public User editUser(User user) throws SQLException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(EDIT_USER_SQL)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.executeUpdate();
        }
        return user;
    }

    public ArrayList<Comment> getComments(long userId) throws SQLException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_COMMENTS_SQL)) {
            statement.setLong(1, userId);
            ArrayList<Comment> comments = new ArrayList<>();
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Comment comment = new Comment(
                        result.getLong("id"),
                        result.getString("full_comment_text"),
                        result.getTimestamp("date_published").toLocalDateTime(),
                        result.getLong("user_id"),
                        result.getLong("article_id"));
                comments.add(comment);
            }
            return comments;
        }
    }

    public User getByUsername(String username) throws SQLException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_USERNAME)) {
            statement.setString(1, username);
            ResultSet rows = statement.executeQuery();
            if (rows.next()) {
                return new User(rows.getLong("id"),
                        rows.getString("user_name"),
                        rows.getString("user_email"),
                        rows.getString("user_password"),
                        rows.getBoolean("is_admin"));
            } else {
                return null;
            }
        }
    }

    public boolean isAdminByUserId(Integer id) throws SQLException {
        boolean isAdmin = false;
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(IS_ADMIN_SQL)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            set.next();
            isAdmin = set.getBoolean("is_admin");
        }
        return isAdmin;
    }

    public User getUserByEmail(String email) throws SQLException {
        User user = null;
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                return null;
            }
            user = new User(set.getInt(1),
                    set.getString(2),
                    set.getString(3),
                    set.getString(4),
                    set.getBoolean(5));
        }
        return user;
    }
}