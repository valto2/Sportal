package example.sportal.controllers;

import com.google.gson.Gson;
import example.sportal.model.dao.PictureDAO;
import example.sportal.model.pojo.POJO;
import example.sportal.model.pojo.PageOfArticle;
import example.sportal.model.pojo.Picture;
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

    @PostMapping(value = "/pictures")
    public void add(@RequestBody Picture picture,
                    HttpServletResponse response,
                    HttpSession session) throws SQLException, IOException {
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("/login");
        }
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (!isAdmin || picture.getUrlOFPicture().isEmpty()) {
            response.setStatus(400);
            response.getWriter().append(WRONG_INFORMATION);
        }
        if (session.getAttribute("articleId") == null) {
            response.sendRedirect("/all_categories_name");
        }
        long articleId = (long) session.getAttribute("articleId");
        boolean checkURLOfPictureExists = this.picturesDAO.existsURLOfPicture(picture.getUrlOFPicture());
        if (checkURLOfPictureExists) {
            response.setStatus(400);
            response.getWriter().append(EXISTS);
        }
        // vasko : setFields from formPictureDTO to picture
        picture.setArticleID(articleId);
        this.picturesDAO.addingOfPictureToTheArticle(picture);
        // vasko : return pictureDTO
    }

    @GetMapping(value = "/pictures/{article_id}")
    public void all(@PathVariable(name = "article_id") Long articleId,
                    HttpServletResponse response,
                    HttpSession session) throws SQLException, IOException {
        Collection<POJO> listFromPictures = this.picturesDAO.allById(articleId);
        PageOfArticle pageOfArticle = (PageOfArticle) session.getAttribute("pageOfArticle");
        pageOfArticle.setPictures(listFromPictures);
        session.setAttribute("pageOfArticle", pageOfArticle);
        response.sendRedirect("/comments/" + articleId);
    }

    // vasko : delete
}
