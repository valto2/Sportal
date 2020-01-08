package example.sportal.model.dto.article;

import example.sportal.model.pojo.Article;
import example.sportal.model.pojo.Category;
import example.sportal.model.pojo.Comment;
import example.sportal.model.pojo.Picture;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class ReturnFullDataArticleDTO {

    private Article article;
    private Collection<Category> categories;
    private Collection<Picture> pictures;
    private Collection<Comment> comments;
    private int numberOfLikes;
    private String authorName;
}
