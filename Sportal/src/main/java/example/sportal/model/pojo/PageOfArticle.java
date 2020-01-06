package example.sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class PageOfArticle implements POJO{

    private Article article;
    private Collection<POJO> categories;
    private Collection<POJO> pictures;
    private Collection<Comment> comments;
    private int numberOfLikes;
}
