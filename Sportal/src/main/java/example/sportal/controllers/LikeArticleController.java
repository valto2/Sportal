package example.sportal.controllers;

import example.sportal.model.dao.UsersLikeArticlesDAO;
import example.sportal.model.dto.article.ReturnFullDataArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@RestController
public class LikeArticleController extends AbstractController {

    @Autowired
    private UsersLikeArticlesDAO likeArticlesDAO;

    @PostMapping(value = "/likes_articles")
    public String add(HttpServletResponse response,
                      HttpSession session) throws IOException, SQLException {
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("/login");
        }
        long userId = (long) session.getAttribute("userId");
        if (session.getAttribute("articleId") == null) {
            response.sendRedirect("/categories");
        }
        long articleId = (long) session.getAttribute("articleId");
        if (this.likeArticlesDAO.existsInThirdTable(articleId, userId)) {
            return "Without more likes from you on this article!";
        }
        if (this.likeArticlesDAO.addInThirdTable(articleId, userId)) {
            return "Likes article!";
        }
        // vasko : return - what object?
        return "Please try again!";
    }

    @GetMapping(value = "/likes_articles/{article_id}")
    public void all(@PathVariable("article_id") Long articleID,
                    HttpServletResponse response,
                    HttpSession session) throws SQLException, IOException {
        ReturnFullDataArticleDTO returnFullDataArticleDTO = (ReturnFullDataArticleDTO) session.getAttribute(RETURN_ARTICLE);
        returnFullDataArticleDTO.setNumberOfLikes(this.likeArticlesDAO.allById(articleID));
        session.setAttribute(RETURN_ARTICLE, returnFullDataArticleDTO);
        response.sendRedirect("/users/" + returnFullDataArticleDTO.getArticle().getAuthorId());
    }

    // vasko : delete
}
