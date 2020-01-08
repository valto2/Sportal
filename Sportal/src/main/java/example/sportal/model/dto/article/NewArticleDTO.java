package example.sportal.model.dto.article;

import example.sportal.model.pojo.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class NewArticleDTO {

    private long id;
    private String title;
    private String fullText;
    private Timestamp createDateAndTime;
    private int views;
    private long authorId;

    public NewArticleDTO(Article article){
        this.setId(article.getId());
        this.setTitle(article.getTitle());
        this.setFullText(article.getFullText());
        this.setCreateDateAndTime(article.getCreateDateAndTime());
        this.setAuthorId(article.getAuthorId());
        this.setViews(article.getViews());
    }
}
