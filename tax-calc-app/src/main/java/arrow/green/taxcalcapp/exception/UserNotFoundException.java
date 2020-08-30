package arrow.green.taxcalcapp.exception;

/**
 * @author nakulgoyal
 *         28/08/20
 **/
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message);
    }
}


