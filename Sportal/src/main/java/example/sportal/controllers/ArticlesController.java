package example.sportal.controllers;

import com.google.gson.Gson;
import example.sportal.dao.ArticleDAO;
import example.sportal.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static example.sportal.controllers.ResponseConstants.*;

@RestController
public class ArticlesController {

    @Autowired
    private ArticleDAO articlesDAO;

    @PostMapping(value = "/admin/create/article")
    public String createArticle(@RequestBody Article article,
                                HttpServletResponse response,
                                HttpSession session) throws SQLException, IOException {
        if (session.getAttribute("userID") == null) {
            response.sendRedirect("LoginForm.html");
        }
        long userID = (long) session.getAttribute("userID");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (!isAdmin){
            response.setStatus(400);
            return new Gson().toJson(WRONG_INFORMATION);
        }
        if (article.getTitle().isEmpty() || article.getFullText().isEmpty()){
            response.setStatus(400);
            return new Gson().toJson(WRONG_INFORMATION);
        }
        article.setAuthorID(userID);
        this.articlesDAO.addArticle(article);
        return new Gson().toJson("Successful added article!");
    }

    @GetMapping(value = "/articles/{title}")
    public String articleFromSpecificTitle(@PathVariable("title") String title,
                                           HttpServletResponse response,
                                           HttpSession session) throws SQLException {
        title = title.replace("_", " ");
        Article article = this.articlesDAO.articleByTitle(title);
        if (article == null){
            response.setStatus(404);
            return new Gson().toJson(NOT_EXISTS_OBJECT);
        }
        if (article.getAuthorName() == null){
            article.setAuthorName(COPYRIGHT);
        }
        this.articlesDAO.addViewOfSpecificArticleID(article.getId());
        session.setAttribute("articleID", article.getId());
        return new Gson().toJson(article);
    }

    @GetMapping(value = "/articles/all_articles_by_title_or_category/{text}")
    public String searchOfTitleOfArticles(@PathVariable("text") String titleOrCategory,
                                          HttpServletResponse response) throws SQLException {
        titleOrCategory = titleOrCategory.replace("_", " ");
        List<String> listOfAllTittleOfArticles = this.articlesDAO.allArticleByTitleOrCategory(titleOrCategory);
        if (listOfAllTittleOfArticles.isEmpty()){
            response.setStatus(404);
            return new Gson().toJson(NOT_EXISTS_OBJECT);
        }
        return new Gson().toJson(listOfAllTittleOfArticles);
    }

    @GetMapping(value = "/articles/top_5_view_articles")
    public String topFiveViewedArticlesToday(HttpServletResponse response) throws SQLException {
        List<Article> listOfArticles = this.articlesDAO.topFiveMostViewedArticlesForToday();
        if (listOfArticles.isEmpty()){
            response.setStatus(404);
            return new Gson().toJson(NOT_EXISTS_OBJECT);
        }
        return new Gson().toJson(listOfArticles);
    }

    @GetMapping(value = "/articles/category/{categoryName}")
    public String articleFromSpecificCategory(@PathVariable("categoryName") String category,
                                              HttpServletResponse response) throws SQLException {
        category = category.replace("_", " ");
        List<Article> ListOfArticles = this.articlesDAO.allArticlesToASpecificCategory(category);
        if (ListOfArticles.isEmpty()){
            response.setStatus(404);
            return new Gson().toJson(NOT_EXISTS_OBJECT);
        }
        return new Gson().toJson(ListOfArticles);
    }

    @GetMapping(value = "/articles/all_title")
    public String getAllTitleOfArticles(HttpServletResponse response) throws SQLException {
        Collection listOfTitle = this.articlesDAO.all();
        if (listOfTitle.isEmpty()){
            response.setStatus(404);
            return NOT_EXISTS_OBJECT;
        }
        return new Gson().toJson(listOfTitle);
    }
}
