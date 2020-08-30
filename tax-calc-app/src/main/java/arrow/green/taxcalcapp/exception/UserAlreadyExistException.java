package arrow.green.taxcalcapp.exception;

/**
 * @author nakulgoyal
 *         29/08/20
 **/
public class UserAlreadyExistException extends RuntimeException{
    
    public UserAlreadyExistException(String message) {
        super(message);
    }
}


