package example.sportal.model.repository;

import example.sportal.model.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {

    public boolean existsArticleById(long id);
}
