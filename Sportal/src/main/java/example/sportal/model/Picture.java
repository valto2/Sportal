package example.sportal.model;

public class Picture {

    private int id;
    private String urlOFPicture;
    private int articleID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlOFPicture() {
        return urlOFPicture;
    }

    public void setUrlOFPicture(String urlOFPicture) {
        this.urlOFPicture = urlOFPicture;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }
}
