package example.sportal.controllers;

import example.sportal.exceptions.AuthorizationException;
import example.sportal.exceptions.BadRequestException;
import example.sportal.exceptions.NotExistsObjectExceptions;
import example.sportal.model.dao.*;
import example.sportal.model.dto.article.*;
import example.sportal.model.dto.category.CategoryResponseDTO;
import example.sportal.model.dto.picture.PictureDTO;
import example.sportal.model.pojo.Article;
import example.sportal.model.pojo.Category;
import example.sportal.model.pojo.Picture;
import example.sportal.model.pojo.User;
import example.sportal.model.validations.ArticleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class ArticleController extends AbstractController {

    @Autowired
    private ArticleDAO articlesDAO;
    @Autowired
    private ArticleValidator validator;
    @Autowired
    private PictureDAO pictureDAO;
    @Autowired
    private ArticlesCategoriesDAO articlesCategoriesDAO;
    @Autowired
    private UsersLikeArticlesDAO likeArticlesDAO;


    @PostMapping(value = "/articles")
    public ArticleAfterCreateDTO addArticle(@RequestBody ArticleCreateDTO articleCreateDTO,
                                            HttpSession session) throws SQLException, BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (!user.getIsAdmin()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        ArticleCreateDTO validArticle = this.validator.checkArticleForValidData(articleCreateDTO);
        Article article = new Article(validArticle);
        article.setAuthorId(user.getId());
        article = this.articlesDAO.addArticle(article);
        List<Picture> pictureList = Picture.fromPictureDTOToPicture(validArticle.getPictures());
        this.pictureDAO.addArticleIdToAllPictures(pictureList, article.getId());
        List<Picture> listFromPicturesAfterSetArticleId = this.pictureDAO.allPicturesByArticleId(article.getId());
        List<PictureDTO> pictureDTOList = PictureDTO.fromPictureToPictureDTO(listFromPicturesAfterSetArticleId);
        this.articlesCategoriesDAO.addListFromCategoriesToArticleId(validArticle.getCategories(), article.getId());
        return new ArticleAfterCreateDTO(article, validArticle.getCategories(), pictureDTOList, user.getId());
    }

    @GetMapping(value = "/articles/search/{" + TITLE_OR_CATEGORY + "}")
    public List<ArticleRespDTO> searchOfArticlesByTitleOfCategoryName(
            @PathVariable(TITLE_OR_CATEGORY) String titleOrCategory) throws SQLException {
        List<Article> listFromArticles = this.articlesDAO.allArticlesByTitleOrCategory(titleOrCategory);
        if (listFromArticles.isEmpty()) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
        List<ArticleRespDTO> listFromReturnArticle = new ArrayList<>();
        for (Article a : listFromArticles) {
            listFromReturnArticle.add(new ArticleRespDTO(a));
        }
        return listFromReturnArticle;
    }

    @GetMapping(value = "/articles/{" + ARTICLE_ID + "}")
    public ArticleFullDataDTO articleBySpecificTitle(
            @PathVariable(ARTICLE_ID) long articleId) throws SQLException, BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        ArticleFullDataDTO viewArticle = new ArticleFullDataDTO();
        Article article = this.articlesDAO.articleById(articleId);
        if (article == null) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
        viewArticle.setArticle(new ArticleWithViewsAndFullTextDTO(article));
        List<Category> categories = this.articlesCategoriesDAO.allCategoriesByArticlesId(articleId);
        viewArticle.setCategories(CategoryResponseDTO.fromCategoryListToCategoryResponseDTO(categories));
        List<Picture> pictures = this.pictureDAO.allPicturesByArticleId(articleId);
        viewArticle.setPictures(PictureDTO.fromPictureToPictureDTO(pictures));
        viewArticle.setNumberOfLikes(this.likeArticlesDAO.totalLikesByArticleId(articleId));
        if (article.getAuthorName() == null) {
            viewArticle.setAuthorName(COPYRIGHT);
        } else {
            viewArticle.setAuthorName(article.getAuthorName());
        }
        this.articlesDAO.addViewOfByArticleId(articleId);
        return viewArticle;
    }

    @GetMapping(value = "/articles/the_category/{" + CATEGORY_ID + "}")
    public List<ArticleRespDTO> articlesByCategoryId(
            @PathVariable(CATEGORY_ID) long categoryId) throws SQLException, BadRequestException {
        if (categoryId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        List<Article> articles = this.articlesCategoriesDAO.allArticlesByCategoryId(categoryId);
        if (articles == null || articles.isEmpty()) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
        return ArticleRespDTO.fromArticleToArticleRespDTO(articles);
    }

    @GetMapping(value = "/articles/top_5_view_articles")
    public List<ArticleRespDTO> topFiveViewedArticlesToday() throws SQLException {
        List<Article> listFromArticles = this.articlesDAO.topFiveMostViewedArticlesForToday();
        if (listFromArticles.isEmpty()) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
        List<ArticleRespDTO> listFromReturnArticle = new ArrayList<>();
        for (Article a : listFromArticles) {
            listFromReturnArticle.add(new ArticleRespDTO(a));
        }
        return listFromReturnArticle;
    }


    @PutMapping(value = "/articles")
    public ArticleAfterEditDTO editArticleTitleOrText(@RequestBody ArticleEditDTO articleEditDTO,
                                                      HttpSession session) throws SQLException, BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (!user.getIsAdmin()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        ArticleEditDTO validArticle = this.validator.validationBeforeEdit(articleEditDTO);
        Article article = new Article(validArticle);
        if (this.articlesDAO.editOfTitleAndFullText(article) > 0) {
            return new ArticleAfterEditDTO(article);
        } else {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
    }

    @DeleteMapping(value = "/articles/{" + ARTICLE_ID + "}")
    public long deleteArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                              HttpSession session) throws SQLException, BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if (!user.getIsAdmin()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        Article article = this.articlesDAO.articleById(articleId);
        if (article == null) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
        this.articlesDAO.deleteById(articleId);
        return articleId;
    }
}