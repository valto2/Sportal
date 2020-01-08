package example.sportal.controllers;

import example.sportal.exceptions.AuthorizationException;
import example.sportal.model.dao.UserDAO;
import example.sportal.model.dto.LoginUserDTO;
import example.sportal.model.dto.RegisterUserDTO;
import example.sportal.model.dto.UserWithoutPasswordDTO;
import example.sportal.model.pojo.User;
import example.sportal.model.validations.UserValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
<<<<<<< HEAD
public class UserController extends AbstractController {
=======
public class UserController extends AbstractController{

>>>>>>> d20b3cf0a57f896e373941ee381bcefc0e44d0c1
    @Autowired
    private UserDAO userDAO;

    @PostMapping("/users")
    public UserWithoutPasswordDTO register(@RequestBody RegisterUserDTO userDto, HttpSession session) throws SQLException {
        //TODO validate data in userDto
        //create User object
        User user = new User(userDto);
        //add to database
        userDAO.registerUser(user);
        //return UserWithoutPasswordDTO
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, user);
        UserWithoutPasswordDTO responseDto = new UserWithoutPasswordDTO(user);
        return responseDto;
    }

    @PostMapping("/users/login")
    public UserWithoutPasswordDTO login(@RequestBody LoginUserDTO userDTO, HttpSession session) throws SQLException {
        User user = userDAO.getByUsername(userDTO.getUsername());
        if (user == null) {
            throw new AuthorizationException("Invalid credentials");
        } else if (UserValidations.isPasswordValid(userDTO.getPassword())) {
            session.setAttribute(LOGGED_USER_KEY_IN_SESSION, user);
            UserWithoutPasswordDTO responseDto = new UserWithoutPasswordDTO(user);
            return responseDto;
        } else {
            throw new AuthorizationException("Invalid credentials");
        }
    }

    @PostMapping("/users/logout")
    public void login(HttpSession session) {
        session.invalidate();
    }

}




//    @GetMapping("/index")
//    public String index() {
//        return "home";
//    }
//
//    @GetMapping("/login")
//    public String login(Model model) {
//        return "login";
//    }
//
//    //login
//    @PostMapping("/login")
//    public String login(@RequestBody User user,
//                        Model model,
//                        HttpSession session) {
//
//        if (!UserValidations.isEMailValid(user.getEmail())) {
//            model.addAttribute("error", "E-mail address is not valid!");
//            return "login";
//        }
//        try {
//            User loginUser = UserDAO.getInstance().getUserByEmail(user.getEmail());
//            if (loginUser != null && loginUser.getPassword().equals(user.getPassword())) {
//                session.setAttribute("userID", loginUser.getId());
//                session.setAttribute("isAdmin", loginUser.getIsAdmin());
//                model.addAttribute("msg", "success");
//                System.out.println(loginUser.toString());
//                if (loginUser.getIsAdmin()) {
//                    return "admin";
//                }
//                return "home";
//            } else {
//                model.addAttribute("error", "Invalid credentials!"); //password does not match
//                return "login";
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//        model.addAttribute("error", "Error");
//        return "login";
//    }
//
//    @GetMapping("/register")
//    public String register(Model model) {
//        return "register";
//    }
//
//    //register
//    @PostMapping("/register")
//    public String register(@RequestParam String username,
//                           @RequestParam String email,
//                           @RequestParam String password,
//                           @RequestParam String password2,
//                           Model model) {
//
//        if (!UserValidations.isUsernameValid(username)) {
//            model.addAttribute("error", "Username should be at least 4 characters long");
//            return "register";
//        }
//
//        if (!UserValidations.isEMailValid(email)) {
//            model.addAttribute("error", "E-mail address should be a valid one!");
//            return "register";
//        }
//
//        if (!UserValidations.isPasswordValid(password)) {
//            model.addAttribute("error", "Password should contains at least one digit, " +
//                    "at least one lower case character, at least one upper case character and " +
//                    "at least one special character from [@ # $ % ! .]");
//            return "register";
//        }
//
//        if (!password.equals(password2)) {
//            model.addAttribute("error", "The passwords does not match!");
//            return "register";
//        }
//
//        try {
//            if (!UserDAO.getInstance().checkIfUserExists(email)) {
//                User user = new User(username, password, email);
//                UserDAO.getInstance().registerUser(user);
//                model.addAttribute("msg", "success");
//                return "login";
//            } else {
//                model.addAttribute("error", "User with that e-mail already exists");
//                return "register";
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        model.addAttribute("error", "Error");
//        return "register";
//    }
//
//    //logout
//    @GetMapping("/logout")
//    public String logout(HttpSession session, HttpServletResponse response, Model model) {
//        if (session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return "login";
//        }
//        session.setAttribute("userId", null);
//        model.addAttribute("msg", "success");
//        return "login";
//    }
//
//    //update Personal info
//    @PostMapping("/updateInfo")
//    public void updateUserInfo(@RequestBody User user, HttpSession session, HttpServletResponse response, Model model) {
//        if (session.getAttribute("userId") == null) {
//            model.addAttribute("error", "you should be logged in");
//            response.setStatus(405);
//            return;
//        }
//        try {
//            UserDAO.getInstance().updateUserInfo(user);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//}

