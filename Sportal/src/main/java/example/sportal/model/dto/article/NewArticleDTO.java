package example.sportal.model.dto.article;

import example.sportal.model.pojo.Article;
import example.sportal.model.pojo.Category;
import example.sportal.model.pojo.Picture;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class NewArticleDTO {

    private long id;
    private String title;
    private String fullText;
    private Timestamp createDateAndTime;
    private Collection<Category> categories;
    private Collection<Picture> pictures;
    private int views;
    private long authorId;

    public NewArticleDTO(Article article, Collection<Category> categories, Collection<Picture> pictures){
        this.setId(article.getId());
        this.setTitle(article.getTitle());
        this.setFullText(article.getFullText());
        this.setCreateDateAndTime(article.getCreateDateAndTime());
        this.setAuthorId(article.getAuthorId());
        this.setViews(article.getViews());
        this.setCategories(categories);
        this.setPictures(pictures);
    }
}
