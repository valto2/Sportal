package example.sportal.exceptions;

public class ExistsObjectException extends RuntimeException {

    public ExistsObjectException(String messages){
        super(messages);
    }
}
