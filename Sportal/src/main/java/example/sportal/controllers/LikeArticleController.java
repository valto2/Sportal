package example.sportal.controllers;

import com.google.gson.Gson;
import example.sportal.dao.UsersLikeArticlesDAO;
import example.sportal.model.PageOfArticle;
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
            response.sendRedirect("/user/loginForm");
        }
        long userID = (long) session.getAttribute("userID");
        if (session.getAttribute("articleID") == null) {
            response.sendRedirect("/all_categories");
        }
        long articleID = (long) session.getAttribute("articleID");
        if (this.likeArticlesDAO.existsInThirdTable(articleID, userID)) {
            return new Gson().toJson("Without more likes from you on this article!");
        }
        if (this.likeArticlesDAO.addInThirdTable(articleID, userID)) {
            return "Likes article!";
        }
        return "Please try again!";
    }

    @GetMapping(value = "/all_like/article/{article_id}")
    public void totalLikesByArticleID(@PathVariable("article_id") Long articleID,
                                      HttpServletResponse response,
                                      HttpSession session) throws SQLException, IOException {
        PageOfArticle pageOfArticle = (PageOfArticle) session.getAttribute("pageOfArticle");
        pageOfArticle.setNumberOfLikes(this.likeArticlesDAO.allByID(articleID));
        session.setAttribute("pageOfArticle", pageOfArticle);
        response.sendRedirect("/all_data_of_articles");
    }
}
