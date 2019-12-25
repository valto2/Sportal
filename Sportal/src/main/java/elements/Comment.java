package elements;

import java.time.LocalDateTime;

public class Comment {

    private int id;
    private String fullCommentText;
    private LocalDateTime timePosted;
    private int userID;
    private int articleID;
    private int reply_id; // add in DB

    public Comment(int id, String fullCommentText, LocalDateTime createDateAndTime, int userID, int articleID) {
        this.id = id;
        this.fullCommentText = fullCommentText;
        this.timePosted = createDateAndTime;
        this.userID = userID;
        this.articleID = articleID;
    }

    public int getReply_id() {
        return reply_id;
    }

    public void setReply_id(int reply_id) {
        this.reply_id = reply_id;
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

    public LocalDateTime getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(LocalDateTime timePosted) {
        this.timePosted = timePosted;
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
