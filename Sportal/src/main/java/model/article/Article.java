package elements;

import java.sql.Timestamp;

public class Article {

    private int id;
    private String title;
    private String fullText;
    private Timestamp createDateAndTime;
    private int views;
    private int authorID;

    public Article() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public Timestamp getCreateDateAndTime() {
        return createDateAndTime;
    }

    public void setCreateDateAndTime(Timestamp createDateAndTime) {
        this.createDateAndTime = createDateAndTime;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }
}
