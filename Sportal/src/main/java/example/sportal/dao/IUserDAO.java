package example.sportal.dao;

import example.sportal.exceptions.UserException;
import example.sportal.model.User;
import java.sql.SQLException;

public interface IUserDAO {

    void registerUser(User user) throws SQLException;
//    int login(String email, String password) throws UserException;
    boolean checkIfUserExists(String email) throws SQLException;
    void updateUserInfo(User user) throws SQLException;
    User getUserByEmail(String email) throws SQLException;
    boolean isAdminByUserId(Integer id) throws SQLException;

}