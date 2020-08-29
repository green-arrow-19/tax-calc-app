package arrow.green.taxcalcapp.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;
import lombok.Data;

/**
 * @author nakulgoyal
 *         28/08/20
 **/

@Data
@Validated
public class SignUpRequest {
    
    @NotEmpty
    @Email
    @Size(min = 5, max = 30)
    private String username;
    @NotEmpty
    @Size(min = 8, max = 12)
    private String password;
    @NotEmpty
    @Size(min = 2, max = 30)
    private String firstName;
    @Size(min = 2, max = 30)
    private String middleName;
    @Size(min = 2, max = 30)
    private String lastName;
    @Size(min = 2, max = 30)
    private String prefix;
}
