package example.sportal.model.repository;

import example.sportal.model.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface commentRepository extends JpaRepository<Comment,Long> {

    public Comment getCommentById(long id);
}
