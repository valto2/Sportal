package example.sportal.controllers;

import com.google.gson.Gson;
import example.sportal.dao.CategoryDAO;
import example.sportal.model.Category;
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
public class CategoriesController {

    @Autowired
    private CategoryDAO categoriesDAO;

    @PostMapping(value = "/admin/add/category")
    public String addCategory(@RequestBody Category category,
                              HttpServletResponse response,
                              HttpSession session) throws SQLException, IOException {
        if (session.getAttribute("userID") == null) {
            response.sendRedirect("LoginForm.html");
        }
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (!isAdmin) {
            response.setStatus(400);
            return new Gson().toJson(WRONG_INFORMATION);
        }
        if (category.getCategory().isEmpty()) {
            response.setStatus(400);
            return new Gson().toJson(WRONG_INFORMATION);
        }
        this.categoriesDAO.addingCategory(category.getCategory());
        return new Gson().toJson("Successful added category");
    }

    @GetMapping(value = "/all_categories")
    public String allCategories(HttpServletResponse response) throws SQLException {
        Collection<Object> listWhitCategories = this.categoriesDAO.all();
        if (listWhitCategories == null) {
            response.setStatus(404);
            return new Gson().toJson(NOT_EXISTS_OBJECT);
        }
        return new Gson().toJson(listWhitCategories);
    }

    @GetMapping(value = "/all/categories_of_article")
    public String getAllCategoriesByArticleTitle(HttpServletResponse response,
                                                 HttpSession session) throws SQLException, IOException {
        if (session.getAttribute("articleID") == null) {
            response.sendRedirect("/all_categories");
        }
        long articleID = (long) session.getAttribute("articleID");
        Collection<POJO> listFromCategories = this.categoriesDAO.allByID(articleID);
        if (listFromCategories.isEmpty()) {
            response.setStatus(404);
            return new Gson().toJson(NOT_EXISTS_OBJECT);
        }
        return new Gson().toJson(listFromCategories);
    }
}
