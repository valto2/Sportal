package example.sportal.model.dto.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import example.sportal.model.dto.picture.PictureDTO;
import example.sportal.model.dto.user.UserWithoutPasswordDTO;
import example.sportal.model.pojo.Article;
import example.sportal.model.pojo.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleAfterCreateDTO {

    private long id;
    private String title;
    private String fullText;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDateAndTime;
    private List<Category> categories;
    private List<PictureDTO> pictures;
    private int views;
    private long authorId;

    public ArticleAfterCreateDTO(Article article, List<Category> categories,
                                 List<PictureDTO> pictures,
                                 long authorId){
        this.setId(article.getId());
        this.setTitle(article.getTitle());
        this.setFullText(article.getFullText());
        this.setCreateDateAndTime(article.getCreateDateAndTime().toLocalDateTime());
        this.setViews(article.getViews());
        this.setCategories(categories);
        this.setPictures(pictures);
        this.setAuthorId(authorId);
    }
}
