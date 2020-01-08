package example.sportal.model.dto.article;

import example.sportal.model.pojo.Category;
import example.sportal.model.pojo.Picture;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class CreateArticleDTO {

    private String title;
    private String fullText;
    private Collection<Category> categories;
    private Collection<Picture> pictures;
}
