package example.sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Picture implements POJO {

    private long id;
    private String urlOFPicture;
    private long articleID;
}
