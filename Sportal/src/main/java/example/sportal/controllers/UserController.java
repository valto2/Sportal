package example.sportal.controllers;

import example.sportal.SessionManager;
import example.sportal.exceptions.AuthorizationException;
import example.sportal.exceptions.BadRequestException;
import example.sportal.exceptions.FailedCredentialsException;
import example.sportal.model.dao.UserDAO;
import example.sportal.model.dto.user.ChangePassDTO;
import example.sportal.model.dto.user.LoginUserDTO;
import example.sportal.model.dto.user.RegisterUserDTO;
import example.sportal.model.dto.user.UserWithoutPasswordDTO;
import example.sportal.model.pojo.Comment;
import example.sportal.model.pojo.User;
import example.sportal.model.validations.UserValidations;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;


@RestController
public class UserController extends AbstractController {

    @Autowired
    private UserDAO userDAO;

    @SneakyThrows
    @PostMapping(value = "users/register")
    public UserWithoutPasswordDTO register(@RequestBody RegisterUserDTO userDto, HttpSession session) {
        UserValidations.validateRegisterDto(userDto);
        User user = new User(userDto);
        String encodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        System.out.println("Encoded password: " + encodedPassword);
        user.setPassword(encodedPassword);
        userDAO.addUser(user);
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, user);
        UserWithoutPasswordDTO responseDto = new UserWithoutPasswordDTO(user);
        return responseDto;
    }

    @SneakyThrows
    @PostMapping(value = "users/login")
    public UserWithoutPasswordDTO login(HttpSession session, @RequestBody LoginUserDTO userDTO) {
        User user = userDAO.getByUsername(userDTO.getUsername());
        if (user == null) {
            System.out.println(USER_DOES_NOT_EXISTS);
            throw new BadRequestException(WRONG_INFORMATION);
        }

        if (!BCrypt.checkpw(userDTO.getPassword(), user.getPassword())) {
            throw new BadRequestException(WRONG_INFORMATION);
        }
        SessionManager.logUser(session, user);
        UserWithoutPasswordDTO responseDto = new UserWithoutPasswordDTO(user);
        return responseDto;
    }

    @PostMapping("users/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }
    @SneakyThrows
    @DeleteMapping("users/delete")
    public void delete(HttpSession session){
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        userDAO.deleteUser(user);
    }
    @SneakyThrows
    @PutMapping("users/change_password")
    public UserWithoutPasswordDTO changePassword(HttpSession session, @RequestBody ChangePassDTO changePasswordDto){
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if(user == null){
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (!user.getPassword().equals(changePasswordDto.getOldPassword())){
            throw new FailedCredentialsException("Wrong password!");
        }
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())){
            throw new FailedCredentialsException(PASSWORD_NOT_MATCH);
        }
        user.setPassword(changePasswordDto.getNewPassword());
        userDAO.editUser(user);
        return new UserWithoutPasswordDTO(user);
    }
    @SneakyThrows
    @GetMapping("users/comments")
    public ArrayList<Comment> getComments(HttpSession session){
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if(user == null){
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        return userDAO.getComments(user.getId());
    }
}

