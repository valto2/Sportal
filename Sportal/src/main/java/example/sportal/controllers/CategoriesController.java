package example.sportal.controllers;

import example.sportal.model.dao.CategoryDAO;
import example.sportal.model.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

<<<<<<< HEAD
import static example.sportal.controllers.AbstractController.NOT_EXISTS_OBJECT;
import static example.sportal.controllers.AbstractController.WRONG_INFORMATION;



=======
>>>>>>> d20b3cf0a57f896e373941ee381bcefc0e44d0c1
@RestController
public class CategoriesController extends AbstractController {

    @Autowired
    private CategoryDAO categoriesDAO;

    @PostMapping(value = "/categories")
    public void add(@RequestBody Category category,
                    HttpServletResponse response, HttpSession session) throws SQLException, IOException {
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("/login");
        }
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (!isAdmin) {
            response.setStatus(400);
            response.getWriter().append(WRONG_INFORMATION);
        }
        if (category.getCategoryName().isEmpty()) {
            response.setStatus(400);
            response.getWriter().append(WRONG_INFORMATION);
        }
        this.categoriesDAO.addCategory(category);
        // vasko : return createCategoryDTO
    }

    // vasko : delete category
    // vasko : edit category

    @GetMapping(value = "/categories")
    public List<Category> all(HttpServletResponse response) throws SQLException, IOException {
        List<Category> listWhitCategories = this.categoriesDAO.allCategories();
        if (listWhitCategories == null) {
            response.setStatus(404);
            response.getWriter().append(NOT_EXISTS_OBJECT);
        }
        // vasko : return listFromCategoryDTO
        return listWhitCategories;
    }

    @GetMapping(value = "/categories/category_name/{name}")
    public void setCategoryIdByTheName(@PathVariable(name = "name") String categoryName,
                                       HttpServletResponse response) throws SQLException, IOException {
        long categoryId = this.categoriesDAO.returnID(categoryName);
        if (categoryId == 0) {
            response.setStatus(404);
            response.getWriter().append(NOT_EXISTS_OBJECT);
        }
    }
}
