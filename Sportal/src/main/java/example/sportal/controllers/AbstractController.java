package example.sportal.controllers;

import example.sportal.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public abstract class AbstractController {

    // key session
    static final String LOGGED_USER_KEY_IN_SESSION = "loggedUser";

    // responses
    static final String LOGIN_MESSAGES = "You must be logged in!";
    static final String SOMETHING_WENT_WRONG = "Please try again!";
    static final String PASSWORD_NOT_MATCH = "Your password does not match!";
    static final String USER_DOES_NOT_EXISTS = "The user does not exists!";
    static final String WRONG_INFORMATION = "Wrong information about the user or empty fields!";
    static final String COPYRIGHT = "Sportal holds the copyright of this article.";
    static final String NOT_EXISTS_OBJECT = "Not found!";
    public static final String WRONG_REQUEST = "Invalid request!";
    static final String WITHOUT_MORE_VOTE = "Without more likes from you on this article!";
    static final String NOT_ALLOWED_OPERATION = "The operation you want to perform is not allowed for you!";

    // parameters
    static final String ARTICLE_ID = "article_id";
    static final String CATEGORY_ID = "category_id";
    static final String PICTURE_ID = "picture_id";
    static final String TITLE_OR_CATEGORY = "title_or_category";

    @ExceptionHandler(WrongCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionObject handlerOfWrongCredentialsException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
        return exceptionObject;
    }

    @ExceptionHandler(FailedCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionObject handlerOfFailedCredentialsException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
        return exceptionObject;
    }

    @ExceptionHandler(ExistsObjectException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionObject handlerOfExistsObjectException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
        return exceptionObject;
    }

    @ExceptionHandler(NotExistsObjectExceptions.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionObject handlerOfNotExistsObjectException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
        return exceptionObject;
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionObject handlerOfAuthorizationException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
        return exceptionObject;
    }

    @ExceptionHandler(TransactionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionObject handlerOfTransactionException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
        return exceptionObject;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionObject handlerOfBadRequestException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
        return exceptionObject;
    }

    @ExceptionHandler(SomethingWentWrongException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionObject handlerOfSomethingWentWrongException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
        return exceptionObject;
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionObject handlerOfIOException(IOException e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
        return exceptionObject;
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionObject handlerOfSQLException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
        return exceptionObject;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public ExceptionObject handlerOfException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(),
                HttpStatus.I_AM_A_TEAPOT.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
        return exceptionObject;
    }
}
