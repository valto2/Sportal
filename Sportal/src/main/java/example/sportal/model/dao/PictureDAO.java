package example.sportal.model.dao;

import example.sportal.exceptions.TransactionException;
import example.sportal.model.dao.interfaceDAO.IDAODeleteById;
import example.sportal.model.pojo.Picture;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class PictureDAO extends DAO implements IDAODeleteById {

    private static final String UPDATE_ARTICLE_ID_INTO_PICTURES_TABLE =
            "UPDATE pictures SET article_id = ? WHERE id = ?;";
    private static final String UPLOAD_SQL = "INSERT INTO pictures (picture_url) VALUES (?, ?);";
    private static final String DELETE_PICTURE_BY_ID = "DELETE FROM pictures WHERE id = ?;";
    private static final String ALL_PICTURES_BY_ARTICLE_ID =
            "SELECT id, picture_url FROM pictures WHERE article_Id = ?;";

    public void uploadOfPicture(Picture pictures) throws SQLException {
        this.jdbcTemplate.update(UPLOAD_SQL, pictures.getUrlOFPicture(), pictures.getArticleID());
    }

    @Override
    public int deleteById(long id) throws SQLException {
        int rowAffected = this.jdbcTemplate.update(DELETE_PICTURE_BY_ID, id);
        return rowAffected;
    }

    public Collection<Picture> allById(long id) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ALL_PICTURES_BY_ARTICLE_ID, id);
        Collection<Picture> listWithPictures = new ArrayList<>();
        while (rowSet.next()) {
            listWithPictures.add(this.createPictureByRowSet(rowSet));
        }
        return listWithPictures;
    }

    private Picture createPictureByRowSet(SqlRowSet rowSet) throws SQLException {
        Picture picture = new Picture();
        picture.setId(rowSet.getLong("id"));
        picture.setUrlOFPicture(rowSet.getString("picture_url"));
        return picture;
    }

    public boolean existsURLOfPicture(String urlOFPicture) {
        String searchSQL = "SELECT id FROM pictures WHERE picture_url = ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(searchSQL, urlOFPicture);
        return rowSet.next();
    }

    public Collection<Picture> addListFromPicturesToArticleId(
            Collection<Picture> pictures, long articleId) throws SQLException {
        Connection connection = this.jdbcTemplate.getDataSource().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_ARTICLE_ID_INTO_PICTURES_TABLE)) {
            connection.setAutoCommit(false);

            // vasko : not finished yet

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new TransactionException("Unsuccessful attempt to add pictures! " + e.getMessage());
            } catch (SQLException ex) {
                throw new SQLException("Unsuccessful connection.rollback()! " + ex.getMessage());
            }
        } finally {
            connection.setAutoCommit(true);
        }
        return null;
    }
}
