package example.sportal.model.dto.article;

import example.sportal.model.pojo.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class ReturnArticleDTO {

    private long id;
    private String title;
    private Timestamp createDateAndTime;

   public ReturnArticleDTO(Article article){
       this.setId(article.getId());
       this.setTitle(article.getTitle());
       this.setCreateDateAndTime(article.getCreateDateAndTime());
   }

}
