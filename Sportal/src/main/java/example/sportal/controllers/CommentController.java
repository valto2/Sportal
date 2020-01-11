package example.sportal.controllers;

import example.sportal.SessionManager;
import example.sportal.exceptions.AuthorizationException;
import example.sportal.exceptions.CommentException;
import example.sportal.exceptions.NotExistsObjectExceptions;
import example.sportal.model.dao.CommentDAO;
import example.sportal.model.dto.comment.RequestCommentDTO;
import example.sportal.model.dto.comment.ResponseCommentDTO;
import example.sportal.model.pojo.Comment;
import example.sportal.model.pojo.User;
import example.sportal.model.repository.ArticleRepository;
import example.sportal.model.repository.CommentRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.SneakyThrows;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
public class CommentController extends AbstractController {
    @Autowired
    private CommentDAO commentDAO;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @SneakyThrows
    @PostMapping(value = "/comments/{article_id}/add")
    public ResponseEntity<ResponseCommentDTO> addComment(HttpSession session,
                                                            @RequestBody RequestCommentDTO requestCommentDto,
                                                            @PathVariable("article_id") long articleId) {
        //checks for being logged in
        if (!SessionManager.validateLogged(session)) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        //checks for video availability
        if(!articleRepository.existsArticleById(articleId)){
            throw new NotExistsObjectExceptions("Video with id "+articleId+" not found!");
        }
        //sets up comment
        Comment comment = new Comment(requestCommentDto, articleId);
        comment.setUserID(SessionManager.getLoggedUser(session).getId());
        commentRepository.save(comment);
        return new ResponseEntity<>(new ResponseCommentDTO(comment), HttpStatus.OK);
    }
    @SneakyThrows
    @PutMapping(value = "/comments/{commentId}")
    public ResponseEntity<ResponseCommentDTO> editComment(HttpSession session,
                                                          @RequestBody RequestCommentDTO requestCommentDto,
                                                          @PathVariable("commentId") long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (!optionalComment.isPresent()) {
            throw new CommentException("Comment with id " + commentId + " not found!");
        }
        if (!SessionManager.validateLogged(session)) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        Comment comment = optionalComment.get();
        if (comment.getUserID() != SessionManager.getLoggedUser(session).getId()) {
            throw new AuthorizationException("You are not the owner of this comment to edit it");
        }
        comment.setFullCommentText(requestCommentDto.getFullCommentText());
        commentRepository.save(comment);
        return new ResponseEntity<>(new ResponseCommentDTO(comment), HttpStatus.OK);
    }

    @SneakyThrows
    @DeleteMapping(value = "/comments/{commentId}")
    public ResponseEntity<String> deleteComment(HttpSession session,
                                                @PathVariable("commentId") long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (!optionalComment.isPresent()) {
            throw new NotExistsObjectExceptions("Comment with id " + commentId + " not found!");
        }
        if (!SessionManager.validateLogged(session)) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        Comment comment = optionalComment.get();
        if (comment.getUserID() != SessionManager.getLoggedUser(session).getId()) {
            throw new AuthorizationException("You are not the owner of this comment to delete it");
        }
        commentDAO.deleteComment(comment);
        return new ResponseEntity<>("Comment with id=" + commentId + " deleted!", HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping(value = "/comments/{comment_id}")
    public ResponseEntity<ResponseCommentDTO> getCommentById(@PathVariable("comment_id") long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (!optionalComment.isPresent()) {
            throw new CommentException("Comment with id=" + commentId + " not found!");
        }
        Comment comment = optionalComment.get();
        return new ResponseEntity(new ResponseCommentDTO(comment), HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping(value = "/comments/{videoId}/{commentId}/like")
    public ResponseEntity<String> likeComment(HttpSession session,
                                              @PathVariable("videoId") long videoId,
                                              @PathVariable("commentId") long commentId){
        if (!SessionManager.validateLogged(session)) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if(!articleRepository.existsArticleById(videoId)){
            throw new NotExistsObjectExceptions("Video with id "+videoId+" not found!");
        }
        if(!commentRepository.existsCommentById(commentId)){
            throw new NotExistsObjectExceptions("Comment with id "+commentId+" not found!");
        }
        Comment comment = commentRepository.getCommentById(commentId);
        User currentUser = SessionManager.getLoggedUser(session);
        String message = commentDAO.likeComment(currentUser, comment);

        return new ResponseEntity <>(message, HttpStatus.OK);
    }
    @SneakyThrows
    @PostMapping(value = "/comments/{videoId}/{commentId}/dislike")
    public ResponseEntity<String> dislikeComment(HttpSession session,
                                                 @PathVariable("videoId") long videoId,
                                                 @PathVariable("commentId") long commentId){
        if (!SessionManager.validateLogged(session)) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        if(!articleRepository.existsArticleById(videoId)){
            throw new NotExistsObjectExceptions("Video with id "+videoId+" not found!");
        }
        if(!commentRepository.existsCommentById(commentId)){
            throw new NotExistsObjectExceptions("Comment with id "+commentId+" not found!");
        }
        Comment comment = commentRepository.getCommentById(commentId);
        User currentUser = SessionManager.getLoggedUser(session);
        String message = commentDAO.dislikeComment(currentUser, comment);

        return new ResponseEntity <>(message, HttpStatus.OK);
    }
}
