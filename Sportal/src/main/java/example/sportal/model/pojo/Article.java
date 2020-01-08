package example.sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class Article implements POJO {

    private long id;
    private String title;
    private String fullText;
    private Timestamp createDateAndTime;
    private int views;
    private String authorName;
    private long authorId;
}
