package example.sportal.dao;

import example.sportal.model.User;

import java.sql.*;

public class UserDAO implements IUserDAO {


    private static UserDAO mInstance;

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        if (mInstance == null) {
            mInstance = new UserDAO();
        }
        return mInstance;
    }


    @Override
    // register
    public void registerUser(User user) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String sql = "INSERT INTO users (user_name, email, password)" +
                " VALUES (?, ?,md5(?));";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        }
    }
    //        VALIDATIONS ===========================================================================
//        try {
//            if (isDuplicateName(user.getUsername())) {
//                throw new UserException("The username is already taken. Please use a different one");
//            }
//            if (isDuplicateEmail(user.getEmail())) {
//                throw new UserException("E-mail is already taken. Please use a different one.");
//            }
//        VALIDATIONS ===========================================================================

    @Override
    public User getUserByEmail(String email) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String sql = "SELECT * FROM users WHERE user_email = ?;";
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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

    @Override
    public boolean isAdminByUserId(Integer id) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String sql = "SELECT is_admin FROM users WHERE id = ?;";
        boolean isAdmin = false;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            set.next();
            isAdmin = set.getBoolean("is_admin");
        }
        return isAdmin;
    }

    @Override
    public boolean checkIfUserExists(String email) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String sql = "SELECT id FROM users WHERE email = ?;";
        boolean exist = false;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            exist = set.next();
        }
        return exist;
    }

    @Override
    public void updateUserInfo(User user) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String sql = "UPDATE users SET  = user_name = ? , emial = ?, password = ? WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        }
    }
}

//    //     check existing name
//    private boolean isDuplicateName(String name) throws SQLException {
//        Connection connection = DBManager.INSTANCE.getConnection();
//        String sql = "SELECT * FROM users WHERE user_name = ?;";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, name);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next() == false) {
//                return false;
//            }
//            return true;
//        }
//    }
//
//    // check existing email
//    private boolean isDuplicateEmail(String email) throws SQLException {
//        Connection connection = DBManager.INSTANCE.getConnection();
//        String sql = "SELECT * FROM users WHERE user_email = ?;";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, email);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next() == false) {
//                return false;
//            }
//            return true;
//        }
//    }
//    @Override
//    public int login(String email, String password) throws UserException {
//        try {
//            Connection connection = DBManager.INSTANCE.getConnection();
//            String sql = "SELECT id FROM users WHERE email = ? AND password = md5(?);";
//            try (PreparedStatement statement = connection.prepareStatement(sql)) {
//                statement.setString(1, email);
//                statement.setString(2, password);
//                ResultSet resultSet = statement.executeQuery();
//                if (!resultSet.next()) {
//                    throw new UserException("Invalid email or password!");
//                }
//                int userId = resultSet.getInt(1);
//                return userId;
//            }
//        } catch (SQLException e) {
//            throw new UserException("Could not login! Please try again later!", e);
//        }
//    }
