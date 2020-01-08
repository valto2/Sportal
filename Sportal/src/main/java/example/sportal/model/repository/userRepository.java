package example.sportal.model.repository;

import example.sportal.model.pojo.Comment;
import example.sportal.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepository extends JpaRepository<User, Long> {

    public Comment getCommentById(long id);
}