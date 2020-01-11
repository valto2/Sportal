package example.sportal.controllers;

import example.sportal.exceptions.AuthorizationException;
import example.sportal.exceptions.NotExistsObjectExceptions;
import example.sportal.exceptions.SomethingWentWrongException;
import example.sportal.model.dao.ArticleDAO;
import example.sportal.model.dao.ArticlesCategoriesDAO;
import example.sportal.model.dao.PictureDAO;
import example.sportal.model.dto.article.*;
import example.sportal.model.pojo.Article;
import example.sportal.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class ArticlesController extends AbstractController {
    @Autowired
    private ArticleDAO articlesDAO;
    @Autowired
    private ArticlesCategoriesDAO articlesCategoriesDAO;
    @Autowired
    private PictureDAO picturesDAO;

    @PostMapping(value = "/articles")
    public NewArticleDTO add(@RequestBody CreateArticleDTO createArticleDTO,
                             HttpSession session) throws SQLException {
        session.setAttribute(CREATE_NEW_ARTICLE, createArticleDTO);

        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (!user.getIsAdmin()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }

        if (createArticleDTO.getTitle().isEmpty() || createArticleDTO.getFullText().isEmpty()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }

        // vasko : not finished yet
        Article article = new Article(createArticleDTO);
        article.setAuthorId(user.getId());
        this.articlesCategoriesDAO.addListFromCategoriesToArticleId(createArticleDTO.getCategories(), article.getId());
        this.picturesDAO.addListFromPicturesToArticleId(createArticleDTO.getPictures(), article.getId());
        if (this.articlesDAO.addArticle(article) > 0) {
            NewArticleDTO newArticleDTO = new NewArticleDTO(
                    article,
                    createArticleDTO.getCategories(),
                    createArticleDTO.getPictures());
            return newArticleDTO;
        } else {
            throw new SomethingWentWrongException(SOMETHING_WENT_WRONG);
        }
    }

    @GetMapping(value = "/articles/{title}")
    public void articleBySpecificTitle(@PathVariable("title") String title,
                                       HttpServletResponse response,
                                       HttpSession session) throws SQLException, IOException {
        Article article = this.articlesDAO.articleByTitle(title);
        if (article == null) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
        this.articlesDAO.addViewOfSpecificArticleID(article.getId());
        ReturnFullDataArticleDTO returnFullDataArticleDTO = new ReturnFullDataArticleDTO();
        returnFullDataArticleDTO.setArticle(article);
        session.setAttribute(RETURN_ARTICLE, returnFullDataArticleDTO);
        response.sendRedirect("/categories/" + article.getId());
    }

    @GetMapping(value = "/articles/search/{titleOrCategory}")
    public List<ReturnArticleDTO> searchOfArticlesByTitleOfCategoryName(
            @PathVariable("titleOrCategory") String titleOrCategory) throws SQLException {

        List<Article> listFromArticles = this.articlesDAO.allArticlesByTitleOrCategory(titleOrCategory);
        if (listFromArticles.isEmpty()) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }

        List<ReturnArticleDTO> listFromReturnArticle = new ArrayList<>();
        for (Article a : listFromArticles) {
            listFromReturnArticle.add(new ReturnArticleDTO(a));
        }
        return listFromReturnArticle;
    }

    @GetMapping(value = "/articles/top_5_view_articles")
    public List<ReturnArticleDTO> topFiveViewedArticlesToday() throws SQLException {
        List<Article> listFromArticles = this.articlesDAO.topFiveMostViewedArticlesForToday();
        if (listFromArticles.isEmpty()) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }

        List<ReturnArticleDTO> listFromReturnArticle = new ArrayList<>();
        for (Article a : listFromArticles) {
            listFromReturnArticle.add(new ReturnArticleDTO(a));
        }
        return listFromReturnArticle;
    }

    @DeleteMapping(value = "/articles")
    public long delete(@RequestBody Long articleId, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (!user.getIsAdmin()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }

        if (this.articlesDAO.deleteById(articleId) > 0) {
            return articleId;
        } else {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
    }

    @PutMapping(value = "/articles")
    public EditArticleDTO edit(@RequestBody EditArticleDTO editArticleDTO,
                               HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (!user.getIsAdmin()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }

        Article article = new Article(editArticleDTO);
        if (this.articlesDAO.edit(article) > 0) {
            return editArticleDTO;
        } else {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
    }
}