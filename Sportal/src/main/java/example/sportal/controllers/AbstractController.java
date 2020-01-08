package example.sportal.controllers;

import example.sportal.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;

public abstract class AbstractController {

    // key session
    static final String LOGGED_USER_KEY_IN_SESSION = "loggedUser";
    static final String RETURN_ARTICLE = "fullDataArticle";
    static final String CREATE_NEW_ARTICLE = "newArticle";
    // responses
    static final String WRONG_CREDENTIALS = "Your username, email or password is wrong!";
    static final String LOGIN_MESSAGES = "You must to log in!";
    static final String SOMETHING_WENT_WRONG = "Please try again!";
    static final String EXISTS = "That object exists!";
    static final String FAILED_CREDENTIALS = "Validate your data is failed!";
    private static final String TRY_AGAIN = "Please try again later!";
    static final String WRONG_INFORMATION = "Wrong information about the user or empty fields!";
    static final String COPYRIGHT = "Sportal holds the copyright of this article.";
    static final String NOT_EXISTS_OBJECT = "Not found!";

    static final String WRONG_REQUEST = "Invalid request!";

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
    public String handlerOfIOException(IOException e) {
        System.out.println(e.getMessage());
        return TRY_AGAIN;
    }

    @ExceptionHandler(ParseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handlerOfParseException(ParseException e) {
        System.out.println(e.getMessage());
        return TRY_AGAIN;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public String handlerOfException(Exception e) {
        System.out.println(e.getMessage());
        return TRY_AGAIN;
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlerOfIllegalStateException(Exception e) {
        System.out.println(e.getMessage());
        return TRY_AGAIN;
    }
}
