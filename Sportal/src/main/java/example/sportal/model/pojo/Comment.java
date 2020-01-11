package example.sportal.model.pojo;

import example.sportal.model.dto.comment.RequestCommentDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fullCommentText;
    private LocalDateTime timePosted;
    private long userID;
    private long articleID;
    private int numberOfLikes;
    private int numberOfDislike;

    public Comment(long id, String fullCommentText, LocalDateTime timePosted, long userID, long articleID) {
        this.id = id;
        this.fullCommentText = fullCommentText;
        this.timePosted = timePosted;
        this.userID = userID;
        this.articleID = articleID;
    }

    public Comment(RequestCommentDTO requestCommentDTO) {
        setTimePosted(requestCommentDTO.getTimePosted());
        setFullCommentText(requestCommentDTO.getFullCommentText());
        setArticleID(requestCommentDTO.getArticleID());
    }

    public Comment(RequestCommentDTO requestCommentDto, long requestArticleId) {
        this.createCommentFromRequestDto(requestCommentDto, requestArticleId);
    }
    private void createCommentFromRequestDto(RequestCommentDTO requestCommentDto,
                                             long requestArticleId){
        this.setFullCommentText(requestCommentDto.getFullCommentText());
        this.setTimePosted(LocalDateTime.now());
        this.setArticleID(requestArticleId);
    }
}



