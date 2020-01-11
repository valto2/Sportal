package example.sportal.model.dto.comment;

import com.sun.istack.NotNull;
import example.sportal.model.pojo.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCommentDTO {

    @NotNull
    private long id;
    @NotNull
    private String fullCommentText;
    @NotNull
    private LocalDate timePosted;
    @NotNull
    private long userID;
    @NotNull
    private String userName;
    @NotNull
    private long articleID;
    @NotNull
    private int numberOfLikes;
    @NotNull
    private int numberOfDislike;

    public ResponseCommentDTO(Comment comment) {
        this.setId(comment.getId());
        this.setFullCommentText(getFullCommentText());
        this.setTimePosted(getTimePosted());
        this.setUserID(getUserID());
        this.setUserName(getUserName());
        this.setArticleID(getArticleID());
        this.setNumberOfDislike(getNumberOfLikes());
        this.setNumberOfDislike(getNumberOfDislike());
    }
}
