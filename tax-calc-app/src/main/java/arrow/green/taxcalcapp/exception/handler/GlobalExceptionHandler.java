package arrow.green.taxcalcapp.exception.handler;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import arrow.green.taxcalcapp.exception.NotAuthorizedException;
import arrow.green.taxcalcapp.exception.UserAlreadyExistException;
import arrow.green.taxcalcapp.exception.UserNotFoundException;
import arrow.green.taxcalcapp.exception.WeakPasswordException;
import arrow.green.taxcalcapp.model.ErrorDetails;
import io.jsonwebtoken.JwtException;

/**
 * @author nakulgoyal
 *         28/08/20
 **/

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDetails> userNotFound(Throwable throwable, HttpServletRequest request) {
        return getCurrentFailureMessage(HttpStatus.NOT_FOUND, throwable.getMessage());
    }
    
    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDetails> userAlreadyExist(Throwable throwable, HttpServletRequest request) {
        return getCurrentFailureMessage(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage());
    }
    
    @ExceptionHandler(WeakPasswordException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDetails> weakPasswordException(Throwable throwable, HttpServletRequest request) {
        return getCurrentFailureMessage(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> argumentNotValidException(Throwable throwable, HttpServletRequest request) {
        return getCurrentFailureMessage(HttpStatus.BAD_REQUEST, throwable.getMessage());
    }
    
    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDetails> notAuthorizedException(Throwable throwable, HttpServletRequest request) {
        return getCurrentFailureMessage(HttpStatus.UNAUTHORIZED, throwable.getMessage());
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> badRequestException(Throwable throwable, HttpServletRequest request) {
        return getCurrentFailureMessage(HttpStatus.BAD_REQUEST, throwable.getMessage());
    }
    
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDetails> jwtException(Throwable throwable, HttpServletRequest request) {
        return getCurrentFailureMessage(HttpStatus.UNAUTHORIZED, throwable.getMessage());
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> badRequestParamException(Throwable throwable, HttpServletRequest request) {
        return getCurrentFailureMessage(HttpStatus.BAD_REQUEST, throwable.getMessage());
    }
    
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDetails> otherException(Throwable throwable, HttpServletRequest request) {
        return getCurrentFailureMessage(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage());
    }
    
    private ResponseEntity<ErrorDetails> getCurrentFailureMessage(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ErrorDetails.builder()
                                                              .message(message)
                                                              .status(status.name())
                                                              .timestamp(new Date())
                                                              .build());
    }
}


