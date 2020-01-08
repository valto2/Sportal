package example.sportal.controllers;

import example.sportal.SessionManager;
import example.sportal.exceptions.AuthorizationException;
import example.sportal.exceptions.BadRequestException;
import example.sportal.model.dao.UserDAO;
import example.sportal.model.dto.LoginUserDTO;
import example.sportal.model.dto.RegisterUserDTO;
import example.sportal.model.dto.UserWithoutPasswordDTO;
import example.sportal.model.pojo.User;
import example.sportal.model.repository.userRepository;
import example.sportal.model.validations.UserValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;


@RestController
public class UserController extends AbstractController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    userRepository userRepository;

    @PostMapping(value = "users/register")
    public UserWithoutPasswordDTO register(@RequestBody RegisterUserDTO userDto, HttpSession session) throws BadRequestException, SQLException {
        UserValidations.validateRegisterDto(userDto);
        User user = new User(userDto);
        String encodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()); /// TODO: 8.1.2020 г. BCrypt !/
        System.out.println("Encoded password: " + encodedPassword);
        user.setPassword(encodedPassword);
        userDAO.addUser(user);
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, user);
        UserWithoutPasswordDTO responseDto = new UserWithoutPasswordDTO(user);
        return responseDto;
    }

    @PostMapping(value = "users/login")
    public UserWithoutPasswordDTO login(HttpSession session, @RequestBody LoginUserDTO userDTO)
            throws BadRequestException {

        User user = userRepository.getByUsername(userDTO.getUsername());
        if (user == null) {
            System.out.println("User doesn't exists");
            throw new BadRequestException("Invalid username or password!");
        }

        if (!BCrypt.checkpw(userDTO.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password!");
        }

        SessionManager.logUser(session, user);
        UserWithoutPasswordDTO responseDto = new UserWithoutPasswordDTO(user);
        return responseDto;  // not sure its working
    }

    @PostMapping("/users/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

}

//    @PostMapping("/users/login")
//        public UserWithoutPasswordDTO login (@RequestBody LoginUserDTO userDTO, HttpSession session) throws SQLException
//        {
//            User user = userRepository.getByUsername(userDTO.getUsername());
//            if (user == null) {
//                throw new AuthorizationException("Invalid credentials");
//            } else if (UserValidations.(userDTO.getPassword());) {
//                session.setAttribute(LOGGED_USER_KEY_IN_SESSION, user);
//                UserWithoutPasswordDTO responseDto = new UserWithoutPasswordDTO(user);
//                return responseDto;
//            } else {
//                throw new AuthorizationException("Invalid credentials");
//            }
//        }
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

