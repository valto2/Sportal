package example.sportal.model.dto.article;

import example.sportal.model.dto.category.CategoryResponseDTO;
import example.sportal.model.dto.picture.PictureDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleFullDataDTO {

    private ArticleWithViewsAndFullTextDTO  article;
    private List<CategoryResponseDTO> categories;
    private List<PictureDTO> pictures;
    private int numberOfLikes;
    private String authorName;
}
