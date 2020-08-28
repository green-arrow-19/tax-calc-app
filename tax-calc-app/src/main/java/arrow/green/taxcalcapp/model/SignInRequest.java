package arrow.green.taxcalcapp.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;
import lombok.Data;

/**
 * @author nakulgoyal
 *         28/08/20
 **/

@Data
@Validated
public class SignInRequest {
    
    @NotEmpty
    @Email
    private String username;
    @NotEmpty
    private String password;
}
