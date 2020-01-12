package example.sportal.model.pojo;

import example.sportal.model.dto.article.ArticleAfterCreateDTO;
import example.sportal.model.dto.article.ArticleCreateDTO;
import example.sportal.model.dto.article.ArticleEditDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String fullText;
    private Timestamp createDateAndTime;
    private int views;
    private long authorId;
    private String authorName;

    public  Article (ArticleEditDTO articleEditDTO){
        this.setId(articleEditDTO.getArticleBeforeEditDTO().getId());
        this.setTitle(articleEditDTO.getNewTitle());
        this.setFullText(articleEditDTO.getNewFullText());
        this.setCreateDateAndTime(Timestamp.valueOf(LocalDateTime.now()));
    }

    public Article(ArticleCreateDTO validArticle) {
        this.setTitle(validArticle.getTitle());
        this.setFullText(validArticle.getFullText());
        this.setCreateDateAndTime(Timestamp.valueOf(LocalDateTime.now()));
        this.setViews(0);
    }
}
