package example.sportal.dao;

import example.sportal.dao.interfaceDAO.IDAOAllInfo;
import example.sportal.dao.interfaceDAO.IDAOAllPOJOByID;
import example.sportal.dao.interfaceDAO.IDAODeleteByID;
import example.sportal.model.Category;
import example.sportal.model.POJO;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class CategoryDAO extends DAO implements IDAODeleteByID, IDAOAllPOJOByID, IDAOAllInfo {


    public void addingCategory(String category) throws SQLException {
        String insertCategorySQL = "INSERT INTO categories (category_name) VALUES (?);";
        this.jdbcTemplate.update(insertCategorySQL, category);
    }

    @Override
    public Collection<POJO> allByID(long id) throws SQLException {
        String allCategories =
                "SELECT c.id, c.category_name " +
                        "FROM categories AS c " +
                        "JOIN articles_categories AS aa ON c.id = aa.category_id " +
                        "JOIN articles AS a ON a.id = aa.article_id " +
                        "WHERE article_Id = ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(allCategories, id);
        Collection<POJO> listWithCategories = new ArrayList<>();
        while (rowSet.next()) {
            listWithCategories.add(this.createCategoryByRowSet(rowSet));
        }

        return listWithCategories;
    }

    private Category createCategoryByRowSet(SqlRowSet rowSet){
        Category category = new Category();
        category.setId(rowSet.getInt("id"));
        category.setCategory(rowSet.getString("category_name"));
        return category;
    }

    @Override
    public int deleteByID(long id) throws SQLException {
        String deleteCategorySQL= "DELETE FROM categories WHERE id = ?;";
        return this.jdbcTemplate.update(deleteCategorySQL,id);
    }

    @Override
    public Collection<Object> all() throws SQLException {
        String allCategories = "SELECT id, category_name FROM categories;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(allCategories);
        Collection<Object> listWithCategories = new ArrayList<>();
        while (rowSet.next()) {
            listWithCategories.add(this.createCategoryByRowSet(rowSet));
        }
        return listWithCategories;
    }
}
