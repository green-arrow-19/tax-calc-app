package arrow.green.taxcalcapp.model.dto;

import arrow.green.taxcalcapp.model.PersonalDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nakulgoyal
 *         29/08/20
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String username;
    private PersonalDetails personalDetails;
}


