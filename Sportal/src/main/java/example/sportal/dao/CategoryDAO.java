package example.sportal.dao;

import example.sportal.dao.interfaceDAO.IDAOAllInfo;
import example.sportal.dao.interfaceDAO.IDAODeleteByID;
import example.sportal.dao.interfaceDAO.IDAOReturnPOJOIDIfExistsInTable;
import example.sportal.model.Category;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CategoryDAO extends DAO
        implements IDAODeleteByID, IDAOAllInfo, IDAOReturnPOJOIDIfExistsInTable {


    public void addCategory(Category category) throws SQLException {
        String insertCategorySQL = "INSERT INTO categories (category_name) VALUES (?);";
        this.jdbcTemplate.update(insertCategorySQL, category.getCategoryName());
    }

    @Override
    public int deleteByID(long id) throws SQLException {
        String deleteCategorySQL= "DELETE FROM categories WHERE id = ?;";
        return this.jdbcTemplate.update(deleteCategorySQL,id);
    }

    @Override
    public Collection<String> all() throws SQLException {
        String allCategoryNamesSQL = "SELECT category_name FROM categories;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(allCategoryNamesSQL);
        Collection<String> listWithCategories = new ArrayList<>();
        while (rowSet.next()) {
            listWithCategories.add(rowSet.getString("category_name"));
        }
        return listWithCategories;
    }

    @Override
    public long returnID(String name) throws SQLException {
        String returnCategoryIDSQL = "SELECT id FROM categories WHERE category_name = ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(returnCategoryIDSQL, name);
        if (rowSet.next()){
            return rowSet.getLong("id");
        }
        return 0;
    }

    public List<Category> allCategories() {
        String allCategoriesSQL = "SELECT id, category_name FROM categories;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(allCategoriesSQL);
        List<Category> listFromCategories = new ArrayList<>();
        while (rowSet.next()){
            listFromCategories.add(this.createCategoryByRowSet(rowSet));
        }
        return listFromCategories;
    }

    private Category createCategoryByRowSet(SqlRowSet rowSet){
        Category category = new Category();
        category.setId(rowSet.getInt("id"));
        category.setCategoryName(rowSet.getString("category_name"));
        return category;
    }
}
