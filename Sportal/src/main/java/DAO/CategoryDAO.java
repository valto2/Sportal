package DAO;

import elements.Category;
import model.article.Article;
import model.db.DBManager;

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
                "INSERT INTO categories (category_name) " +
                        "VALUES (?);";

        try (PreparedStatement statement = connection.prepareStatement(insertCategorySQL)) {
            statement.setString(1, category);
            statement.executeUpdate();

            System.out.println("Successful added of category!");
        }
    }

    public void addingCategoryToArticle(String category, Article article) throws SQLException, SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        Category temporaryCategory = this.findCategoryIDByText(category);
        if(temporaryCategory != null) {
            String insertCategoryToArticleSQL =
                    "INSERT INTO articles_categories (article_id ,category_id) " +
                            "VALUES (?,?);";

            try (PreparedStatement statement = connection.prepareStatement(insertCategoryToArticleSQL)) {
                statement.setInt(1, article.getId());
                statement.setInt(2, temporaryCategory.getId());
                statement.executeUpdate();

                System.out.println("Successfully added a category to an article");
            }
        }
    }

    private Category findCategoryIDByText(String category) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String allCategories = "SELECT c.id, c.category_name FROM categories AS c;";

        ArrayList<Category> listWithCategories = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(allCategories)) {
            ResultSet row = statement.executeQuery();
            while (row.next()) {
                Category tempCategory = new Category();
                tempCategory.setId(row.getInt("c.id"));
                tempCategory.setCategory(row.getString("c.category_name"));
                listWithCategories.add(tempCategory);
            }
        }

        return this.checkingForExist(listWithCategories, category);
    }

    private Category checkingForExist(ArrayList<Category> listWithCategories, String category) {
        for (Category c: listWithCategories){
            if (c.getCategory().equals(category)){
                return c;
            }
        }
        return null;
    }

    public ArrayList<String> allCategoriesToASpecificArticle(Article article) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String allCategories = "SELECT" +
                " c.category_name " +
                "FROM categories AS c " +
                "JOIN article_ategories AS aa ON c.id = aa.category_id " +
                "JOIN articles AS a ON a.id = aa.article_id " +
                "WHERE article_Id = ?;";

        ArrayList<String> listWithCategories = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(allCategories)) {
            statement.setInt(1, article.getId());
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
        Category temporaryCategory = this.findCategoryIDByText(category);
        ArrayList<Article> listWithArticles = new ArrayList<>();
        if(temporaryCategory != null) {
            String allArticles = "SELECT " +
                    "a.id, " +
                    "a.title, " +
                    "a.full_text, " +
                    "a.date_published, " +
                    "a.views, " +
                    "a.author_id, " +
                    "a.admin_id, " +
                    "FROM articles AS a " +
                    "JOIN article_ategories AS aa ON a.id = aa.article_id " +
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
                    article.setCreateDateAndTime(row.getTimestamp("a.date_published").toLocalDateTime());
                    article.setViews(row.getInt("a.views"));
                    article.setAuthorID(row.getInt("a.author_id"));
                    article.setAdminID(row.getInt("a.admin_id"));

                    listWithArticles.add(article);
                }
            }
        }

        return listWithArticles;
    }
}
