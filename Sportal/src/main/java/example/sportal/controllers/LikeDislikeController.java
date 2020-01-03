package example.sportal.controllers;

import com.google.gson.Gson;
import example.sportal.dao.UsersLikeArticlesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@RestController
public class LikeDislikeController {

    @Autowired
    private UsersLikeArticlesDAO likeArticlesDAO;

    @PostMapping(value = "/user/like/comment")
    public void likeComment(HttpServletResponse response, HttpSession session) throws IOException {
        if (session.getAttribute("userID") == null) {
            response.sendRedirect("LoginForm.html");
        }
    }

    @PostMapping(value = "/user/dislike/comment")
    public void dislikeComment(HttpServletResponse response, HttpSession session) throws IOException {
        if (session.getAttribute("userID") == null) {
            response.sendRedirect("LoginForm.html");
        }
    }

    @PostMapping(value = "/user/like/article")
    public String likeArticle(HttpServletResponse response,  HttpSession session) throws IOException, SQLException {
        long userID = -1;
        if (session.getAttribute("userID") == null) {
           // response.sendRedirect("LoginForm.html");
        }else {
            userID = (long) session.getAttribute("userID");
        }
        long articleID = -1;
        if(session.getAttribute("articleID") == null){
           // response.sendRedirect("/articles/top_5_view_articles");
        }else {
            articleID = (long) session.getAttribute("articleID");
        }
        if(this.likeArticlesDAO.existsInThirdTable(articleID, userID)){
            return new Gson().toJson("Without more likes from you on this article!");
        }
        this.likeArticlesDAO.addInThirdTable(articleID, userID);
        return new Gson().toJson("Likes article!");
    }
}
