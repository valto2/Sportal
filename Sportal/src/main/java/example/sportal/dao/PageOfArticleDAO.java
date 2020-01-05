package example.sportal.dao;

import example.sportal.model.Article;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class PageOfArticleDAO extends DAO {

    private Article createArticleByRowSet(SqlRowSet rowSet) {
        Article article = new Article();
        article.setId(rowSet.getLong("id"));
        article.setTitle(rowSet.getString("title"));
        article.setFullText(rowSet.getString("full_text"));
        article.setCreateDateAndTime(rowSet.getTimestamp("date_published"));
        article.setViews(rowSet.getInt("views") + 1);
        article.setAuthorID(rowSet.getLong("author_id"));
        if (rowSet.getString("user_name") != null) {
            article.setAuthorName(rowSet.getString("user_name"));
        }
        return article;
    }
}
