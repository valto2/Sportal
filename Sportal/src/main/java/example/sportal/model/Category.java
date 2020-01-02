package example.sportal.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Category implements POJO {

    private int id;
    private String category;
}
