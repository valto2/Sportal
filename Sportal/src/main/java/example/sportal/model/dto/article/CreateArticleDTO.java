package example.sportal.model.dto.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateArticleDTO {

    private String title;
    private String fullText;
}
