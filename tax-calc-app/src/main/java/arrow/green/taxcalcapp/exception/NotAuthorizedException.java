package arrow.green.taxcalcapp.exception;

/**
 * @author nakulgoyal
 *         30/08/20
 **/
public class NotAuthorizedException extends RuntimeException {
    
    public NotAuthorizedException(String message) {
        super(message);
    }
}


