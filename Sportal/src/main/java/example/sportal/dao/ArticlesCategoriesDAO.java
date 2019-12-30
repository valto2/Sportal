package example.sportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArticlesCategoriesDAO {

    private static ArticlesCategoriesDAO instance = new ArticlesCategoriesDAO();

    private ArticlesCategoriesDAO() {
    }

    public static ArticlesCategoriesDAO getInstance() {
        return instance;
    }

    public void categoryArticle(int categoryID, int articleID) throws SQLException, SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String insertCategoryToArticleSQL = "INSERT INTO articles_categories (article_id ,category_id) VALUES (?,?);";

        try (PreparedStatement statement = connection.prepareStatement(insertCategoryToArticleSQL)) {
            statement.setInt(1, categoryID);
            statement.setInt(2, articleID);
            int rowAffected = statement.executeUpdate();

            String success = rowAffected + " row, successfully added!";
            System.out.println(success);
        }
    }
}
