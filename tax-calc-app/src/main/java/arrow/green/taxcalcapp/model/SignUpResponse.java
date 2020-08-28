package arrow.green.taxcalcapp.model;

import lombok.Builder;
import lombok.Getter;

/**
 * @author nakulgoyal
 *         28/08/20
 **/

@Getter
public class SignUpResponse extends CommonResponse{
    
    private final String username;
    private final PersonalDetails personalDetails;
    
    @Builder
    public SignUpResponse(String status, String username, PersonalDetails personalDetails) {
        super(status);
        this.username = username;
        this.personalDetails = personalDetails;
    }
}
