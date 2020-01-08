package example.sportal.controllers;

import example.sportal.model.dao.UserDAO;
import example.sportal.model.pojo.User;
import example.sportal.model.validations.UserValidations;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserController {

    @GetMapping("/index")
    public String index() {
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    //login
    @PostMapping("/login")
    public String login(@RequestBody User user,
                        Model model,
                        HttpSession session) {

        if (!UserValidations.isEMailValid(user.getEmail())) {
            model.addAttribute("error", "E-mail address is not valid!");
            return "login";
        }
        try {
            User loginUser = UserDAO.getInstance().getUserByEmail(user.getEmail());
            if (loginUser != null && loginUser.getPassword().equals(user.getPassword())) {
                session.setAttribute("userID", loginUser.getId());
                session.setAttribute("isAdmin", loginUser.getIsAdmin());
                model.addAttribute("msg", "success");
                System.out.println(loginUser.toString());
                if (loginUser.getIsAdmin()) {
                    return "admin";
                }
                return "home";
            } else {
                model.addAttribute("error", "Invalid credentials!"); //password does not match
                return "login";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        model.addAttribute("error", "Error");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    //register
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String password2,
                           Model model) {

        if (!UserValidations.isUsernameValid(username)) {
            model.addAttribute("error", "Username should be at least 4 characters long");
            return "register";
        }

        if (!UserValidations.isEMailValid(email)) {
            model.addAttribute("error", "E-mail address should be a valid one!");
            return "register";
        }

        if (!UserValidations.isPasswordValid(password)) {
            model.addAttribute("error", "Password should contains at least one digit, " +
                    "at least one lower case character, at least one upper case character and " +
                    "at least one special character from [@ # $ % ! .]");
            return "register";
        }

        if (!password.equals(password2)) {
            model.addAttribute("error", "The passwords does not match!");
            return "register";
        }

        try {
            if (!UserDAO.getInstance().checkIfUserExists(email)) {
                User user = new User(username, password, email);
                UserDAO.getInstance().registerUser(user);
                model.addAttribute("msg", "success");
                return "login";
            } else {
                model.addAttribute("error", "User with that e-mail already exists");
                return "register";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("error", "Error");
        return "register";
    }

    //logout
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response, Model model) {
        if (session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return "login";
        }
        session.setAttribute("userId", null);
        model.addAttribute("msg", "success");
        return "login";
    }

    //update Personal info
    @PostMapping("/updateInfo")
    public void updateUserInfo(@RequestBody User user, HttpSession session, HttpServletResponse response, Model model) {
        if (session.getAttribute("userId") == null) {
            model.addAttribute("error", "you should be logged in");
            response.setStatus(405);
            return;
        }
        try {
            UserDAO.getInstance().updateUserInfo(user);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

