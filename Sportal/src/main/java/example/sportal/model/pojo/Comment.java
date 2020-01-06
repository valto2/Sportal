package example.sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class Comment {

    private long id;
    private String fullCommentText;
    private Timestamp timePosted;
    private long userID;
    private String userName;
    private long articleID;
    private long reply_id;
    private int numberOfLikes;
    private int numberOfDislike;
}
