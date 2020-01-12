package example.sportal.model.dto.article;

import example.sportal.model.dto.picture.PictureDTO;
import example.sportal.model.pojo.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleCreateDTO {

    private String title;
    private String fullText;
    private List<Category> categories;
    private List<PictureDTO> pictures;
}
