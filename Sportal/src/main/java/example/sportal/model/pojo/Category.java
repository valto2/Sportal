package example.sportal.model.pojo;

import example.sportal.model.dto.category.NewCategoryDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Category {

    private long id;
    private String categoryName;

    public Category(NewCategoryDTO newCategoryDTO){
        setCategoryName(newCategoryDTO.getCategoryName());
    }
}
