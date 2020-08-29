package arrow.green.taxcalcapp.model;

import arrow.green.taxcalcapp.model.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

/**
 * @author nakulgoyal
 *         28/08/20
 **/

@Getter
public class SignInResponse extends CommonResponse {
    
    private final UserDto userDto;
    
    @Builder
    public SignInResponse(String status, UserDto userDto) {
        super(status);
        this.userDto = userDto;
    }
}
