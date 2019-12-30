package example.sportal.model;

import java.sql.Timestamp;

public class Comment {

    private int id;
    private String fullCommentText;
    private Timestamp timePosted;
    private int userID;
    private int articleID;
    private int reply_id; // add in DB


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

    public Timestamp getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(Timestamp timePosted) {
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
