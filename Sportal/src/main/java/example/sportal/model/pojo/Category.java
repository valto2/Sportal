package example.sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Category {

    private int id;
    private String categoryName;
    private long articleId;
}
