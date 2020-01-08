package example.sportal.exceptions;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String messages){
        super(messages);
    }
}
