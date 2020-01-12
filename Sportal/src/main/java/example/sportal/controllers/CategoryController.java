package example.sportal.controllers;

import example.sportal.exceptions.AuthorizationException;
import example.sportal.exceptions.BadRequestException;
import example.sportal.exceptions.NotExistsObjectExceptions;
import example.sportal.model.dao.CategoryDAO;
import example.sportal.model.dto.category.CategoryEditDTO;
import example.sportal.model.dto.category.CategoryResponseDTO;
import example.sportal.model.pojo.Category;
import example.sportal.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class CategoryController extends AbstractController {

    @Autowired
    private CategoryDAO categoriesDAO;

    @PutMapping(value = "/categories")
    public CategoryResponseDTO editCategories(@RequestBody CategoryEditDTO categoryEditDTO,
                                              HttpSession session) throws SQLException, BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (!user.getIsAdmin()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        if (categoryEditDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (categoryEditDTO.getOldCategory() == null ||
                categoryEditDTO.getNewCategoryName() == null ||
                categoryEditDTO.getNewCategoryName().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        Category category = new Category(categoryEditDTO);
        if (this.categoriesDAO.editById(category) > 0) {
            return new CategoryResponseDTO(this.categoriesDAO.findById(category.getId()));
        } else {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
    }

    @GetMapping(value = "/categories")
    public List<CategoryResponseDTO> allCategory() throws SQLException {
        List<Category> listWhitCategories = this.categoriesDAO.allCategories();
        if (listWhitCategories == null) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
        return CategoryResponseDTO.fromCategoryListToCategoryResponseDTO(listWhitCategories);
    }

    @DeleteMapping(value = "/categories/{" + CATEGORY_ID + "}")
    public long deleteCategory(@PathVariable(name = CATEGORY_ID) long categoryId,
                               HttpSession session) throws SQLException, BadRequestException {
        if (categoryId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (!user.getIsAdmin()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        Category category = this.categoriesDAO.findById(categoryId);
        if (category == null) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
        this.categoriesDAO.deleteById(categoryId);
        return categoryId;
    }
}
