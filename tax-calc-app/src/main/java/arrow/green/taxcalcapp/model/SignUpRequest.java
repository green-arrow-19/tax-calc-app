package arrow.green.taxcalcapp.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * @author nakulgoyal
 *         28/08/20
 **/

@Data
public class SignUpRequest {
    
    @NotEmpty
    @Email
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String firstName;
    private String middleName;
    private String lastName;
    private String prefix;
}
