package example.sportal.controllers;

import com.google.gson.Gson;
import example.sportal.dao.ArticlesCategoriesDAO;
import example.sportal.model.Category;
import example.sportal.model.POJO;
import example.sportal.model.PageOfArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import static example.sportal.controllers.ResponseConstants.*;

@RestController
public class ArticlesCategoriesController {

    @Autowired
    private ArticlesCategoriesDAO articlesCategoriesDAO;

    @PostMapping(value = "/admin/add_category/to_article")
    public String addCategoryToArticle(@RequestBody Category category,
                                       HttpServletResponse response,
                                       HttpSession session) throws IOException, SQLException {
        if (session.getAttribute("userID") == null) {
            response.sendRedirect("/user/loginForm");
        }
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (!isAdmin) {
            response.setStatus(400);
            return WRONG_INFORMATION;
        }
        if (category == null || category.getId() == 0) {
            response.setStatus(400);
            return WRONG_REQUEST;
        }
        if (session.getAttribute("articleID") == null) {
            response.sendRedirect("/articles/all_title");
        }
        long articleID = (long) session.getAttribute("articleID");
        if (this.articlesCategoriesDAO.existsInThirdTable(articleID, category.getId())) {
            return EXISTS;
        }
        if (this.articlesCategoriesDAO.addInThirdTable(articleID, category.getId())) {
            return "Added category to article!";
        }
        return "Please try again!";
    }

    @GetMapping(value = "/all_articles/by_categoryID")
    public String articleFromSpecificCategory(HttpServletResponse response,
                                              HttpSession session) throws SQLException, IOException {
        if (session.getAttribute("categoryID") == null) {
            response.sendRedirect("/all_categories_name");
        }
        long categoryID = (long) session.getAttribute("categoryID");
        Collection<POJO> ListFromTitleOfArticles = this.articlesCategoriesDAO.allArticlesByCategoryID(categoryID);
        if (ListFromTitleOfArticles.isEmpty()) {
            response.setStatus(404);
            return new Gson().toJson(NOT_EXISTS_OBJECT);
        }
        return new Gson().toJson(ListFromTitleOfArticles);
    }

    @GetMapping(value = "/all/categories_of_articleID/{article_id}")
    public void getAllCategoriesByArticleTitle(@PathVariable(name = "article_id") Long articleID,
                                               HttpServletResponse response,
                                               HttpSession session) throws SQLException, IOException {
        Collection<POJO> listFromCategories = this.articlesCategoriesDAO.allCategoriesByArticlesID(articleID);
        PageOfArticle pageOfArticle = (PageOfArticle) session.getAttribute("pageOfArticle");
        pageOfArticle.setCategories(listFromCategories);
        session.setAttribute("pageOfArticle", pageOfArticle);
        response.sendRedirect("/all/pictures_of_article/" + articleID);
    }
}
