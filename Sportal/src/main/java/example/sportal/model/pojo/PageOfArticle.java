package example.sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class PageOfArticle {

    private Article article;
    private Collection<Category> categories;
    private Collection<Picture> pictures;
    private Collection<Comment> comments;
    private int numberOfLikes;
}
