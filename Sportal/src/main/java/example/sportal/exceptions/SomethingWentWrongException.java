package example.sportal.exceptions;

public class SomethingWentWrongException extends RuntimeException {

    public SomethingWentWrongException(String messages){
        super(messages);
    }
}
