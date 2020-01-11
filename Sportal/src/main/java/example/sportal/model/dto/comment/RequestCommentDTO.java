package example.sportal.model.dto.comment;

import com.sun.istack.NotNull;
import example.sportal.model.pojo.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCommentDTO {


    @NotNull
    private LocalDateTime timePosted;
    @NotNull
    private String fullCommentText;
    @NotNull
    private long articleID;

}

