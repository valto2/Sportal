package example.sportal.model.pojo;

import example.sportal.model.dto.article.CreateArticleDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class Article {

    private long id;
    private String title;
    private String fullText;
    private Timestamp createDateAndTime;
    private int views;
    private long authorId;

    public Article (CreateArticleDTO createArticleDTO){
        this.setTitle(createArticleDTO.getTitle());
        this.setFullText(createArticleDTO.getFullText());
    }
}
