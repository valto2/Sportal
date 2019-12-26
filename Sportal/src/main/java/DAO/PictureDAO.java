package DAO;

import elements.Picture;
import model.db.DBManager;

import java.sql.*;
import java.util.ArrayList;

public class PictureDAO {

    private static PictureDAO instance = new PictureDAO();

    private PictureDAO() {
    }

    public static PictureDAO getInstance() {
        return instance;
    }

    public void addingOfPicturesToTheArticle(ArrayList<Picture> pictures, int articleID) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();

        String insertPictureSQL = "INSERT INTO pictures (picture_url, article_id) VALUES (?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(insertPictureSQL)) {
            connection.setAutoCommit(false);

            for (Picture p : pictures) {
                statement.setString(1, p.getUrlOFPicture());
                statement.setInt(2, articleID);
                statement.executeUpdate();
            }

            connection.commit();
            System.out.println("Successful added Of pictures!");
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Unsuccessful attempt to add pictures! " + e.getMessage());
            } catch (SQLException ex) {
                throw new SQLException("Unsuccessful connection.rollback()! " + ex.getMessage());
            }
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public ArrayList<Picture> allPicturesToASpecificArticle(int articleID) throws SQLException {
        Connection connection = DBManager.INSTANCE.getConnection();
        String allPictures = "SELECT p.id, p.picture_url FROM pictures AS p WHERE article_Id = ?;";

        ArrayList<Picture> listWithPictures = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(allPictures)) {
            statement.setInt(1, articleID);
            ResultSet row = statement.executeQuery();
            while (row.next()) {
                Picture p = new Picture();
                p.setId(row.getInt("p.id"));
                p.setUrlOFPicture(row.getString("p.picture_url"));
                p.setArticleID(articleID);
                listWithPictures.add(p);
            }
        }

        return listWithPictures;
    }
}
