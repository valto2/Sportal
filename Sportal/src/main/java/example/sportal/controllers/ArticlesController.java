package example.sportal.controllers;

import example.sportal.model.dao.ArticleDAO;
import example.sportal.model.pojo.Article;
import example.sportal.model.pojo.PageOfArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static example.sportal.controllers.AbstractController.*;


@RestController
public class ArticlesController {

    @Autowired
    private ArticleDAO articlesDAO;

    @PostMapping(value = "/articles")
    public void add(@RequestBody Article article,
                    HttpServletResponse response,
                    HttpSession session) throws SQLException, IOException {
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("/login");
        }
        long userId = (long) session.getAttribute("userId");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (!isAdmin) {
            response.setStatus(400);
            response.getWriter().append(WRONG_INFORMATION);
        }
        if (article.getTitle().isEmpty() || article.getFullText().isEmpty()) {
            response.setStatus(400);
            response.getWriter().append(WRONG_INFORMATION);
        }
        article.setAuthorId(userId);
        this.articlesDAO.addArticle(article);
        // vasko : return createdArticleDTO
    }

    // vasko : delete article
    // vasko : edit article

    @GetMapping(value = "/articles/{title}")
    public void articleFromSpecificTitle(@PathVariable("title") String title,
                                         HttpServletResponse response,
                                         HttpSession session) throws SQLException, IOException {
        Article article = this.articlesDAO.articleByTitle(title);
        if (article == null) {
            response.setStatus(404);
            response.getWriter().append(NOT_EXISTS_OBJECT);
        }
        if (article.getAuthorName() == null) {
            article.setAuthorName(COPYRIGHT);
        }
        this.articlesDAO.addViewOfSpecificArticleID(article.getId());
        session.setAttribute("articleId", article.getId());
        PageOfArticle pageOfArticle = new PageOfArticle();
        pageOfArticle.setArticle(article);
        session.setAttribute("pageOfArticle", pageOfArticle);
        response.sendRedirect("/categories/" + article.getId());
    }

    @GetMapping(value = "/articles/search/{titleOrCategory}")
    public List<String> searchOfTitleOfArticles(@PathVariable("titleOrCategory") String titleOrCategory,
                                                HttpServletResponse response) throws SQLException, IOException {
        List<String> listOfAllTittleOfArticles = this.articlesDAO.allArticleByTitleOrCategory(titleOrCategory);
        if (listOfAllTittleOfArticles.isEmpty()) {
            response.setStatus(404);
            response.getWriter().append(NOT_EXISTS_OBJECT);
        }
        // vasko : return articleDTO
        return listOfAllTittleOfArticles;
    }

    @GetMapping(value = "/articles/top_5_view_articles")
    public List<String> topFiveViewedArticlesToday(HttpServletResponse response) throws SQLException, IOException {
        List<String> listOfArticles = this.articlesDAO.topFiveMostViewedArticlesForToday();
        if (listOfArticles.isEmpty()) {
            response.setStatus(404);
            response.getWriter().append(NOT_EXISTS_OBJECT);
        }
        // vasko : return articleDTO
        return listOfArticles;
    }

    @GetMapping(value = "/articles")
    public Collection<String> getAllTitleOfArticles(HttpServletResponse response) throws SQLException, IOException {
        Collection<String> listOfTitle = this.articlesDAO.all();
        if (listOfTitle.isEmpty()) {
            response.setStatus(404);
            response.getWriter().append(NOT_EXISTS_OBJECT);
        }
        // vasko : return articleDTO
        return listOfTitle;
    }
}
