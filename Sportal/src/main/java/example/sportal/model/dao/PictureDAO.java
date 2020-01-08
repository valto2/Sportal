package example.sportal.model.dao;

import example.sportal.model.dao.interfaceDAO.IDAODeleteById;
import example.sportal.model.pojo.Picture;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class PictureDAO extends DAO implements IDAODeleteById {

    public void addingOfPictureToTheArticle(Picture pictures) throws SQLException {
        String insertSQL = "INSERT INTO pictures (picture_url, article_id) VALUES (?, ?);";
        this.jdbcTemplate.update(insertSQL, pictures.getUrlOFPicture(), pictures.getArticleID());
    }

    @Override
    public int deleteById(long id) throws SQLException {
        String deleteSQL = "DELETE FROM pictures WHERE id = ?;";
        int rowAffected = this.jdbcTemplate.update(deleteSQL, id);
        return rowAffected;
    }

    public Collection<Picture> allById(long id) throws SQLException {
        String allSQL = "SELECT id, picture_url, article_Id FROM pictures WHERE article_Id = ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(allSQL,id);
        Collection<Picture> listWithPictures = new ArrayList<>();
        while (rowSet.next()){
            listWithPictures.add(this.createPictureByRowSet(rowSet));
        }
        return listWithPictures;
    }

    private Picture createPictureByRowSet(SqlRowSet rowSet) throws SQLException {
        Picture picture = new Picture();
        picture.setId(rowSet.getLong("id"));
        picture.setUrlOFPicture(rowSet.getString("picture_url"));
        picture.setArticleID(rowSet.getLong("article_Id"));
        return picture;
    }

    public boolean existsURLOfPicture(String urlOFPicture) {
        String searchSQL = "SELECT id FROM pictures WHERE picture_url = ?;";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(searchSQL, urlOFPicture);
        return rowSet.next();
    }
}
