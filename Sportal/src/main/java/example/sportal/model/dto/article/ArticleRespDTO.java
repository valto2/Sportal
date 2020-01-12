package example.sportal.model.dto.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import example.sportal.model.pojo.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleRespDTO {

    private long id;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDateAndTime;

   public ArticleRespDTO(Article article){
       this.setId(article.getId());
       this.setTitle(article.getTitle());
       this.setCreateDateAndTime(article.getCreateDateAndTime().toLocalDateTime());
   }

    public static List<ArticleRespDTO> fromArticleToArticleRespDTO(List<Article> articles) {
       List<ArticleRespDTO> articleRespDTOList = new ArrayList<>();
       for (Article article : articles){
           articleRespDTOList.add(new ArticleRespDTO(article));
       }
        return articleRespDTOList;
    }
}
