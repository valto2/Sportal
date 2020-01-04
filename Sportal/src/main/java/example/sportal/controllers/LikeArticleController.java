package example.sportal.controllers;

import com.google.gson.Gson;
import example.sportal.dao.UsersLikeArticlesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@RestController
public class LikeArticleController {

    @Autowired
    private UsersLikeArticlesDAO likeArticlesDAO;

    @PostMapping(value = "/user/like/article")
    public String likeArticle(HttpServletResponse response,
                              HttpSession session) throws IOException, SQLException {
        if (session.getAttribute("userID") == null) {
            response.sendRedirect("LoginForm.html");
        }
        long userID = (long) session.getAttribute("userID");
        if (session.getAttribute("articleID") == null) {
            response.sendRedirect("/all_categories");
        }
        long articleID = (long) session.getAttribute("articleID");
        if (this.likeArticlesDAO.existsInThirdTable(articleID, userID)) {
            return new Gson().toJson("Without more likes from you on this article!");
        }
        this.likeArticlesDAO.addInThirdTable(articleID, userID);
        return new Gson().toJson("Likes article!");
    }

    @GetMapping(value = "/all_like/article")
    public int totalLikesByArticleID(HttpServletResponse response,
                                     HttpSession session) throws SQLException, IOException {
        if (session.getAttribute("articleID") == null) {
            response.sendRedirect("/all_categories");
        }
        long articleID = (long) session.getAttribute("articleID");
        return this.likeArticlesDAO.allByID(articleID);
    }
}
