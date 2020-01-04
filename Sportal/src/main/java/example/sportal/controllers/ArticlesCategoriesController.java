package example.sportal.controllers;

import com.google.gson.Gson;
import example.sportal.dao.ArticlesCategoriesDAO;
import example.sportal.model.POJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import static example.sportal.controllers.ResponseConstants.NOT_EXISTS_OBJECT;
import static example.sportal.controllers.ResponseConstants.WRONG_INFORMATION;

@RestController
public class ArticlesCategoriesController {

    @Autowired
    private ArticlesCategoriesDAO articlesCategoriesDAO;

    @PostMapping(value = "/admin/add_category/to_article")
    public String addCategoryToArticle(HttpServletResponse response,
                                       HttpSession session) throws IOException, SQLException {
        if (session.getAttribute("userID") == null) {
            response.sendRedirect("LoginForm.html");
        }
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (!isAdmin) {
            response.setStatus(400);
            return new Gson().toJson(WRONG_INFORMATION);
        }
        if (session.getAttribute("categoryID") == null){
            response.sendRedirect("/all_categories_name");
        }
        long categoryID = (long) session.getAttribute("categoryID");
        if (session.getAttribute("articleID") == null) {
            response.sendRedirect("/articles/all_title");
        }
        long articleID = (long) session.getAttribute("articleID");
        this.articlesCategoriesDAO.addInThirdTable(articleID, categoryID);
        return new Gson().toJson("Added category to article!");
    }

    @GetMapping(value = "/all_articles/by_categoryID")
    public String articleFromSpecificCategory(HttpServletResponse response,
                                              HttpSession session) throws SQLException, IOException {
        if (session.getAttribute("categoryID") == null) {
            response.sendRedirect("/all_categories");
        }
        long categoryID = (long) session.getAttribute("categoryID");
        Collection<POJO> ListFromTitleOfArticles = this.articlesCategoriesDAO.allArticlesByCategoryID(categoryID);
        if (ListFromTitleOfArticles.isEmpty()) {
            response.setStatus(404);
            return new Gson().toJson(NOT_EXISTS_OBJECT);
        }
        return new Gson().toJson(ListFromTitleOfArticles);
    }

    @GetMapping(value = "/all/categories_of_articleID")
    public String getAllCategoriesByArticleTitle(HttpServletResponse response,
                                                 HttpSession session) throws SQLException, IOException {
        if (session.getAttribute("articleID") == null) {
            response.sendRedirect("/all_categories");
        }
        long articleID = (long) session.getAttribute("articleID");
        Collection<POJO> listFromNameOfCategories = this.articlesCategoriesDAO.allCategoriesByArticlesID(articleID);
        if (listFromNameOfCategories.isEmpty()) {
            response.setStatus(404);
            return new Gson().toJson(NOT_EXISTS_OBJECT);
        }
        return new Gson().toJson(listFromNameOfCategories);
    }
}
