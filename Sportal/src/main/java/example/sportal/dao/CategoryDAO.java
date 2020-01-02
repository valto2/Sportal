package example.sportal.dao;

import example.sportal.model.Category;
import example.sportal.model.Article;
import example.sportal.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDAO {

    private static CategoryDAO instance = new CategoryDAO();

    private CategoryDAO() {
    }

    public static CategoryDAO getInstance() {
        return instance;
    }

    public void addingCategory(String category) throws SQLException, SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String insertCategorySQL =
                "INSERT INTO categories (category_name) VALUES (?);";

        try (PreparedStatement statement = connection.prepareStatement(insertCategorySQL)) {
            statement.setString(1, category);
            statement.executeUpdate();

            System.out.println("Successful added of a new category!");
        }
    }

    public ArrayList<String> allCategoriesToASpecificArticle(int articleID) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String allCategories = "SELECT c.category_name " +
                "FROM categories AS c " +
                "JOIN articles_categories AS aa ON c.id = aa.category_id " +
                "JOIN articles AS a ON a.id = aa.article_id " +
                "WHERE article_Id = ?;";

        ArrayList<String> listWithCategories = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(allCategories)) {
            statement.setInt(1, articleID);
            ResultSet row = statement.executeQuery();
            while (row.next()) {
                String category = row.getString("c.category_name");

                listWithCategories.add(category);
            }
        }

        return listWithCategories;
    }

    public ArrayList<Article> allArticlesToASpecificCategory(String category) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        Category temporaryCategory = CategoryDAO.findCategoryByText(category);
        ArrayList<Article> listWithArticles = new ArrayList<>();
        if (temporaryCategory != null) {
            String allArticles = "SELECT a.id, a.title, a.full_text, a.date_published, " +
                    "a.views, a.author_id " +
                    "FROM articles AS a " +
                    "JOIN articles_categories AS aa ON a.id = aa.article_id " +
                    "JOIN categories AS c ON aa.category_id = c.id " +
                    "WHERE category_id = ?;";

            try (PreparedStatement statement = connection.prepareStatement(allArticles)) {
                statement.setInt(1, temporaryCategory.getId());
                ResultSet row = statement.executeQuery();
                while (row.next()) {
                    Article article = new Article();
                    article.setId(row.getInt("a.id"));
                    article.setTitle(row.getString("a.title"));
                    article.setFullText(row.getString("a.full_text"));
                    article.setCreateDateAndTime(row.getTimestamp("a.date_published"));
                    article.setViews(row.getInt("a.views"));
                    article.setAuthorID(row.getInt("a.author_id"));

                    listWithArticles.add(article);
                }
            }
        }else {
            String ifCategoryIsNull = "This category does not exists";
            System.out.println(ifCategoryIsNull);
        }

        return listWithArticles;
    }

    public void deleteCategory(int categoryID, User user) throws SQLException {
        if (user.isAdmin()){
            Connection connection = DBManager.INSTANCE.getConnection();
            String deleteCommentSQL= "DELETE FROM categories WHERE id = ?;";
            try (PreparedStatement statement = connection.prepareStatement(deleteCommentSQL)) {
                statement.setInt(1, categoryID);
                int rowAffected = statement.executeUpdate();
                String success = rowAffected + " row, successful delete category!";
                System.out.println(success);
            }
        }else {
            String message = "You are not admin!";
            System.out.println(message);
        }
    }

    private static Category findCategoryByText(String category) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String allCategories = "SELECT c.id, c.category_name FROM categories AS c WHERE c.category_name = ?;";

        Category tempCategory = null;

        try (PreparedStatement statement = connection.prepareStatement(allCategories)) {
            statement.setString(1, category);
            ResultSet row = statement.executeQuery();
            if (row.next()) {
                tempCategory = new Category();
                tempCategory.setId(row.getInt("c.id"));
                tempCategory.setCategory(row.getString("c.category_name"));
            }
        }

        return tempCategory;
    }
}
