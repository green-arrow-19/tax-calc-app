package arrow.green.taxcalcapp.model;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;

/**
 * @author nakulgoyal
 *         28/08/20
 **/

@Builder
@Getter
public class ErrorDetails {
    private final String status;
    private final String message;
    private final Date timestamp;
}


