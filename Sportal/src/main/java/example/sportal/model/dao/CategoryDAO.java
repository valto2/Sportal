package example.sportal.model.dao;

import example.sportal.model.dao.interfaceDAO.IDAODeleteById;
import example.sportal.model.pojo.Category;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryDAO extends DAO implements IDAODeleteById {


    private static final String INSERT_CATEGORY_SQL = "INSERT INTO categories (category_name) VALUES (?);";
    private static final String DELETE_CATEGORY_SQL = "DELETE FROM categories WHERE id = ?;";
    private static final String UPDATE_CATEGORY_NAME = "UPDATE categories SET category_name = ? WHERE id = ?;";
    private static final String FIND_BY_ID = "SELECT category_name FROM categories WHERE id = ?;";
    private static final String ALL_CATEGORIES_SQL = "SELECT id, category_name FROM categories;";

    public int addCategory(Category category) throws SQLException {
        try (
                Connection connection = this.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(INSERT_CATEGORY_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, category.getCategoryName());
            int rowAffected = ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            resultSet.next();
            category.setId(resultSet.getLong(1));
            return rowAffected;
        }
    }

    @Override
    public int deleteById(long id) throws SQLException {
        return this.jdbcTemplate.update(DELETE_CATEGORY_SQL, id);
    }

    public List<Category> allCategories() throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ALL_CATEGORIES_SQL);
        List<Category> listFromCategories = new ArrayList<>();
        while (rowSet.next()) {
            listFromCategories.add(this.createCategoryByRowSet(rowSet));
        }
        return listFromCategories;
    }

    private Category createCategoryByRowSet(SqlRowSet rowSet) throws SQLException {
        Category category = new Category();
        category.setId(rowSet.getLong("id"));
        category.setCategoryName(rowSet.getString("category_name"));
        return category;
    }

    public int editById(Category category) throws SQLException {
        return this.jdbcTemplate.update(UPDATE_CATEGORY_NAME, category.getCategoryName(), category.getId());
    }

    public Category findById(long id) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_BY_ID, id);
        if (rowSet.next()) {
            Category category = new Category();
            category.setId(id);
            category.setCategoryName(rowSet.getString("category_name"));
        }
        return null;
    }
}
