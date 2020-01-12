package example.sportal.model.pojo;

import example.sportal.model.dto.category.CategoryEditDTO;
import example.sportal.model.dto.category.CategoryNewDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Category {

    private long id;
    private String categoryName;

    public Category(CategoryNewDTO newCategoryDTO){
        setCategoryName(newCategoryDTO.getCategoryName());
    }

    public Category(CategoryEditDTO editDTO){
        this.setId(editDTO.getOldCategory().getId());
        this.setCategoryName(editDTO.getNewCategoryName());
    }
}
