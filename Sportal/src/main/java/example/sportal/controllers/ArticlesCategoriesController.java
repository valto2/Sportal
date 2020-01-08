package example.sportal.controllers;

import example.sportal.model.dao.ArticlesCategoriesDAO;
import example.sportal.model.dto.article.ReturnFullDataArticleDTO;
import example.sportal.model.pojo.Article;
import example.sportal.model.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class ArticlesCategoriesController extends AbstractController {

    @Autowired
    private ArticlesCategoriesDAO articlesCategoriesDAO;

    // vasko : delete category by article id
    // vasko : delete article by category id

    @GetMapping(value = "/articles/category/{category_id}")
    public Collection<Article> allArticleByCategoryId(@PathVariable(name = "category_id") Long categoryId,
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
    public void getAllCategoriesByArticleId(@PathVariable(name = "article_id") Long articleId,
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
