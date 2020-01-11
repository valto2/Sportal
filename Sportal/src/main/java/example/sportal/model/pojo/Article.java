package example.sportal.model.pojo;

import example.sportal.model.dto.article.CreateArticleDTO;
import example.sportal.model.dto.article.EditArticleDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String fullText;
    private Timestamp createDateAndTime;
    private int views;
    private long authorId;

    public Article(CreateArticleDTO createArticleDTO) {
        this.setTitle(createArticleDTO.getTitle());
        this.setFullText(createArticleDTO.getFullText());
    }

    public Article(EditArticleDTO editArticleDTO) {
        this.setId(editArticleDTO.getId());
        this.setTitle(editArticleDTO.getTitle());
        this.setFullText(editArticleDTO.getFullText());
    }
}
