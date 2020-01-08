package example.sportal.controllers;

import example.sportal.exceptions.AuthorizationException;
import example.sportal.model.dao.ArticlesCategoriesDAO;
import example.sportal.model.dto.article.ReturnFullDataArticleDTO;
import example.sportal.model.pojo.Article;
import example.sportal.model.pojo.Category;
import example.sportal.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

<<<<<<< HEAD
import static example.sportal.controllers.AbstractController.*;


=======
>>>>>>> d20b3cf0a57f896e373941ee381bcefc0e44d0c1
@RestController
public class ArticlesCategoriesController extends AbstractController {

    @Autowired
    private ArticlesCategoriesDAO articlesCategoriesDAO;

    @PostMapping(value = "/articles/add_category")
    public void addCategoryToArticle(@RequestBody Category category,
                                     HttpServletResponse response,
                                     HttpSession session) throws IOException, SQLException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (!user.getIsAdmin()) {
            response.setStatus(400);
            response.getWriter().append(WRONG_INFORMATION);
        }
        if (category == null || category.getId() == 0 || category.getArticleId() == 0) {
            response.setStatus(400);
            response.getWriter().append(WRONG_REQUEST);
        }
        // vasko : exists article by id and exists category by id
        if (this.articlesCategoriesDAO.existsInThirdTable(category.getArticleId(), category.getId())) {
            response.getWriter().append(EXISTS);
        }
        if (this.articlesCategoriesDAO.addInThirdTable(category.getArticleId(), category.getId())) {
            // vasko : return - what object?
            response.getWriter().append("Added category to article!");
        }
    }

    // vasko : delete category by article id
    // vasko : delete article by category id

    @GetMapping(value = "/articles/category/{category_id}")
    public Collection<Article> articleByCategoryId(@PathVariable(name = "category_id") Long categoryId,
                                                   HttpServletResponse response) throws SQLException, IOException {
        Collection<Article> ListFromTitleOfArticles = this.articlesCategoriesDAO.allArticlesByCategoryID(categoryId);
        if (ListFromTitleOfArticles.isEmpty()) {
            response.setStatus(404);
            response.getWriter().append(NOT_EXISTS_OBJECT);
        }
        // vasko : return articleDTO
        return ListFromTitleOfArticles;
    }

    @GetMapping(value = "/categories/{article_id}")
    public void getAllCategoriesByArticleTitle(@PathVariable(name = "article_id") Long articleId,
                                               HttpServletResponse response,
                                               HttpSession session) throws SQLException, IOException {
        Collection<Category> listFromCategories = this.articlesCategoriesDAO.allCategoriesByArticlesID(articleId);
        ReturnFullDataArticleDTO returnFullDataArticleDTO =
                (ReturnFullDataArticleDTO) session.getAttribute(RETURN_ARTICLE);
        returnFullDataArticleDTO.setCategories(listFromCategories);
        session.setAttribute(RETURN_ARTICLE, returnFullDataArticleDTO);
        response.sendRedirect("/pictures/" + articleId);
    }
}
