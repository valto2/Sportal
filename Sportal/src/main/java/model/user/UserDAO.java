package model.user;

import elements.User;
import model.db.DBManager;
import model.exceptions.UserException;
import org.graalvm.compiler.lir.LIRInstruction;

import java.sql.*;

public class UserDAO {
    public static UserDAO instance = new UserDAO();

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        return instance;
    }

    // register
    public int registerUser(User user) throws UserException {
        try {
            if (isDuplicateName(user.getUsername())) {
                throw new UserException("The username is already taken. Please use a different one");
            }
            if (isDuplicateEmail(user.getEmail())) {
                throw new UserException("E-mail is already taken. Please use a different one.");
            }

            Connection connection = DBManager.INSTANCE.getConnection();
            String sql = "INSERT INTO users (user_name, email, password)" +
                    " VALUES (?, ?,md5(?));";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
                statement.executeUpdate();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                int userId = generatedKeys.getInt(1);
                return userId;
            }
        } catch (SQLException e) {
            throw new UserException("Registration failed. Please try again.", e);
        }
    }

    //     check existing name
    private boolean isDuplicateName(String name) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String sql = "SELECT * FROM users WHERE user_name = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next() == false) {
                return false;
            }
            return true;
        }
    }

    // check existing email
    private boolean isDuplicateEmail(String email) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String sql = "SELECT * FROM users WHERE user_email = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next() == false) {
                return false;
            }
            return true;
        }
    }

    // login
    public int loginUser(String email, String password) throws UserException {
        try {
            Connection connection = DBManager.INSTANCE.getConnection();
            String sql = "SELECT id FROM users WHERE email = ? AND password = md5(?);";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                if (!resultSet.next()) {
                    throw new UserException("Invalid email or password!");
                }
                int userId = resultSet.getInt(1);
                return userId;
            }
        } catch (SQLException e) {
            throw new UserException("Could not login! Please try again later!", e);
        }
    }
}
