package example.sportal.model.dto.article;

import example.sportal.model.pojo.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArticleAfterEditDTO {

    private long id;
    private String newTitle;
    private String newFullText;


    public ArticleAfterEditDTO(Article article) {
        this.setId(article.getId());
        this.setNewTitle(article.getTitle());
        this.setNewFullText(article.getFullText());
    }
}
