package arrow.green.taxcalcapp.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

/**
 * @author nakulgoyal
 *         28/08/20
 **/

@Builder
@Getter
@Embeddable
public class PersonalDetails {
    
    @Column(name = "first_name", nullable = false)
    private final String firstName;
    @Column(name = "middle_name")
    private final String middleName;
    @Column(name = "last_name")
    private final String lastName;
    @Column(name = "prefix")
    private final String prefix;
}
