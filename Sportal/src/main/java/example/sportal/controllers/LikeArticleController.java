package example.sportal.controllers;

import example.sportal.exceptions.AuthorizationException;
import example.sportal.exceptions.BadRequestException;
import example.sportal.exceptions.ExistsObjectException;
import example.sportal.exceptions.SomethingWentWrongException;
import example.sportal.model.dao.UsersLikeArticlesDAO;
import example.sportal.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
public class LikeArticleController extends AbstractController {

    @Autowired
    private UsersLikeArticlesDAO likeArticlesDAO;

    @PostMapping(value = "/users/like_articles/{" + ARTICLE_ID + "}")
    public long likeOfArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                              HttpSession session) throws SQLException, BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (this.likeArticlesDAO.existsInThirdTable(articleId, user.getId())) {
            throw new ExistsObjectException(WITHOUT_MORE_VOTE);
        }
        if (this.likeArticlesDAO.addInThirdTable(articleId, user.getId()) > 0) {
            return articleId;
        } else {
            throw new SomethingWentWrongException(SOMETHING_WENT_WRONG);
        }
    }

    @DeleteMapping(value = "/users/like_articles/{" + ARTICLE_ID + "}")
    public long deleteLikeOfArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                                    HttpSession session) throws SQLException, BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (this.likeArticlesDAO.deleteFromThirdTable(articleId, user.getId()) > 0) {
            return articleId;
        } else {
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
    }
}
