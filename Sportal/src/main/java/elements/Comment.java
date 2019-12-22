package elements;

import java.time.LocalDateTime;

public class Comment {

    private int id;
    private String fullCommentText;
    private LocalDateTime createDateAndTime;
    private int userID;
    private int articleID;

    public Comment(int id, String fullCommentText, LocalDateTime createDateAndTime, int userID, int articleID) {
        this.id = id;
        this.fullCommentText = fullCommentText;
        this.createDateAndTime = createDateAndTime;
        this.userID = userID;
        this.articleID = articleID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullCommentText() {
        return fullCommentText;
    }

    public void setFullCommentText(String fullCommentText) {
        this.fullCommentText = fullCommentText;
    }

    public LocalDateTime getCreateDateAndTime() {
        return createDateAndTime;
    }

    public void setCreateDateAndTime(LocalDateTime createDateAndTime) {
        this.createDateAndTime = createDateAndTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }
}
