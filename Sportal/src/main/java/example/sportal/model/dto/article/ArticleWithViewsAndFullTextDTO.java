package example.sportal.model.dto.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import example.sportal.model.pojo.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ArticleWithViewsAndFullTextDTO {

    private long id;
    private String title;
    private String fullText;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDateAndTime;
    private int views;

    public ArticleWithViewsAndFullTextDTO(Article article) {
        this.setId(article.getId());
        this.setTitle(article.getTitle());
        this.setFullText(article.getFullText());
        this.setCreateDateAndTime(article.getCreateDateAndTime().toLocalDateTime());
        this.setViews(article.getViews());
    }
}
