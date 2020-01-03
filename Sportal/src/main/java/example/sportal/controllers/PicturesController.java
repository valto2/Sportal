package example.sportal.controllers;

import com.google.gson.Gson;
import example.sportal.dao.PictureDAO;
import example.sportal.model.POJO;
import example.sportal.model.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import static example.sportal.controllers.ResponseConstants.*;

@RestController
public class PicturesController {

    @Autowired
    private PictureDAO picturesDAO;

    @PostMapping(value = "/admin/add/picture_to_article")
    public String createPicture(@RequestBody Picture picture,
                                HttpServletResponse response,
                                HttpSession session) throws SQLException, IOException {
        if (session.getAttribute("userID") == null) {
            response.sendRedirect("LoginForm.html");
        }
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (!isAdmin || picture.getUrlOFPicture().isEmpty()) {
            response.setStatus(400);
            return new Gson().toJson(WRONG_INFORMATION);
        }
        if (session.getAttribute("articleID") == null){
            response.sendRedirect("/all_categories");
        }
        long articleID = (long) session.getAttribute("articleID");
        boolean checkURLOfPictureExists = this.picturesDAO.existsURLOfPicture(picture.getUrlOFPicture());
        if (checkURLOfPictureExists) {
            response.setStatus(400);
            return new Gson().toJson(EXISTS);
        }
        picture.setArticleID(articleID);
        this.picturesDAO.addingOfPictureToTheArticle(picture);
        return new Gson().toJson("Successful added picture to article!");
    }

    @GetMapping(value = "/all/pictures_of_article")
    public String getAllPictureByArticleTitle(HttpServletResponse response,
                                              HttpSession session) throws SQLException, IOException {
        if (session.getAttribute("articleID") == null){
            response.sendRedirect("/all_categories");
        }
        long articleID = (long) session.getAttribute("articleID");
        Collection<POJO> listFromPictures = this.picturesDAO.allByID(articleID);
        if (listFromPictures.isEmpty()){
            response.setStatus(404);
            return new Gson().toJson(NOT_EXISTS_OBJECT);
        }
        return new Gson().toJson(listFromPictures);
    }
}
