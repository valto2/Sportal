package example.sportal.model;

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
    private long articleID;
    private long reply_id; // add in DB
}
