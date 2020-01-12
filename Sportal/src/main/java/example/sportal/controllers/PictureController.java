package example.sportal.controllers;

import example.sportal.exceptions.AuthorizationException;
import example.sportal.exceptions.BadRequestException;
import example.sportal.exceptions.NotExistsObjectExceptions;
import example.sportal.model.dao.FileManagerDAO;
import example.sportal.model.dao.PictureDAO;
import example.sportal.model.dto.picture.PictureDTO;
import example.sportal.model.pojo.Picture;
import example.sportal.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PictureController extends AbstractController {

    private static final String PACKAGE_NAME = "C:\\Users\\ACER\\Desktop\\uploadPictures\\";
    private static final String FILE_EXPANSION = ".jpg";
    // date time formatter
    private static final String DATE_AND_TIME_OF_UPLOAD = "date_and_time_of_upload_";

    @Autowired
    private PictureDAO pictureDAO;

    @PostMapping(value = "/pictures")
    public List<PictureDTO> uploadPictures(@RequestPart(value = "picture") List<MultipartFile> multipartFile,
                                           HttpSession session) throws SQLException, BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (!user.getIsAdmin()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        List<Picture> pictures = new ArrayList<>();
        for (MultipartFile mf : multipartFile) {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm.ss.SSS"));
            String urlOfPicture =
                    PACKAGE_NAME + DATE_AND_TIME_OF_UPLOAD + now + FILE_EXPANSION;
            Picture picture = new Picture();
            picture.setUrlOFPicture(urlOfPicture);
            pictures.add(picture);
            FileManagerDAO fileManagerDAO = new FileManagerDAO(mf, urlOfPicture);
            fileManagerDAO.start();
        }
        List<Picture> picturesAfterInsertInDB = this.pictureDAO.uploadOfPictures(pictures);
        return PictureDTO.fromPictureToPictureDTO(picturesAfterInsertInDB);
    }

    @DeleteMapping(value = "/pictures/{" + PICTURE_ID + "}")
    public PictureDTO deletePicture(@PathVariable(name = PICTURE_ID) long pictureId,
                                    HttpSession session) throws SQLException, BadRequestException {
        if (pictureId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (!user.getIsAdmin()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        Picture picture = this.pictureDAO.findPictureById(pictureId);
        if (this.pictureDAO.deletePictureById(pictureId) == 0) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
        File fileForDelete = new File(picture.getUrlOFPicture());
        fileForDelete.delete();
        return new PictureDTO(picture);
    }
}
