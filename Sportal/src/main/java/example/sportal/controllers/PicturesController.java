package example.sportal.controllers;

import example.sportal.model.dao.PictureDAO;
import example.sportal.model.dto.article.ReturnFullDataArticleDTO;
import example.sportal.model.pojo.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

<<<<<<< HEAD
import static example.sportal.controllers.AbstractController.EXISTS;
import static example.sportal.controllers.AbstractController.WRONG_INFORMATION;


=======
>>>>>>> d20b3cf0a57f896e373941ee381bcefc0e44d0c1
@RestController
public class PicturesController extends AbstractController {

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
        Collection<Picture> listFromPictures = this.picturesDAO.allById(articleId);
        ReturnFullDataArticleDTO returnFullDataArticleDTO =
                (ReturnFullDataArticleDTO) session.getAttribute(RETURN_ARTICLE);
        returnFullDataArticleDTO.setPictures(listFromPictures);
        session.setAttribute(RETURN_ARTICLE, returnFullDataArticleDTO);
        response.sendRedirect("/comments/" + articleId);
    }

    // vasko : delete
}
