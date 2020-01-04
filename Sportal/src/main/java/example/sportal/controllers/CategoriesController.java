package example.sportal.controllers;

import com.google.gson.Gson;
import example.sportal.dao.CategoryDAO;
import example.sportal.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static example.sportal.controllers.ResponseConstants.*;


@RestController
public class CategoriesController {

    @Autowired
    private CategoryDAO categoriesDAO;

    @PostMapping(value = "/admin/add/category")
    public String addCategory(@RequestBody Category category,
                              HttpServletResponse response, HttpSession session) throws SQLException, IOException {
        if (session.getAttribute("userID") == null) {
            response.sendRedirect("LoginForm.html");
        }
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (!isAdmin) {
            response.setStatus(400);
            return new Gson().toJson(WRONG_INFORMATION);
        }
        if (category.getCategoryName().isEmpty()) {
            response.setStatus(400);
            return new Gson().toJson(WRONG_INFORMATION);
        }
        this.categoriesDAO.addingCategory(category.getCategoryName());
        return new Gson().toJson("Successful added category");
    }

    @GetMapping(value = "/all_categories")
    public String allCategories(HttpServletResponse response) throws SQLException {
        List<Category> listWhitCategories = this.categoriesDAO.allCategories();
        if (listWhitCategories == null) {
            response.setStatus(404);
            return new Gson().toJson(NOT_EXISTS_OBJECT);
        }
        return new Gson().toJson(listWhitCategories);
    }

    @GetMapping(value = "/category_id/by_category_Name/{category_name}")
    public void setCategoryIDByTheName(@PathVariable(name = "category_name") String categoryName,
                                       HttpServletResponse response,
                                       HttpSession session)throws SQLException, IOException {
        if (categoryName.isEmpty()){
            response.setStatus(400);
            response.getWriter().append(WRONG_REQUEST);
        }
        categoryName = categoryName.replace("_", " ");
        long categoryID = this.categoriesDAO.returnID(categoryName);
        if (categoryID == 0){
            response.setStatus(404);
            response.getWriter().append(NOT_EXISTS_OBJECT);
        }
        session.setAttribute("categoryID", categoryID);
    }

    @GetMapping(value = "/all_categories_name")
    public String allCategoriesName(HttpServletResponse response) throws SQLException {
        Collection<String> listWhitCategoryNames = this.categoriesDAO.all();
        if (listWhitCategoryNames == null) {
            response.setStatus(404);
            return new Gson().toJson(NOT_EXISTS_OBJECT);
        }
        return new Gson().toJson(listWhitCategoryNames);
    }
}
