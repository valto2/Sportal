package example.sportal.model.dto.category;

import example.sportal.model.pojo.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryEditDTO {

    private Category oldCategory;
    private String newCategoryName;
}
