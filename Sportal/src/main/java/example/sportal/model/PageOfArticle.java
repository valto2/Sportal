package example.sportal.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PageOfArticle implements POJO{

    private Article article;
    private String author;
    private List<Category> categories;
    private List<Picture> pictures;
    private List<Comment> comments;
    private int numberOfLikes;
}
