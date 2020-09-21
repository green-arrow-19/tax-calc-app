package arrow.green.taxcalcapp.model.document;

import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import arrow.green.taxcalcapp.model.PersonalDetails;
import arrow.green.taxcalcapp.model.Role;
import arrow.green.taxcalcapp.model.TaxEntry;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nakulgoyal
 *         12/09/20
 **/

@Data
@NoArgsConstructor
@Document(collection = "user")
@CompoundIndexes(value = { @CompoundIndex(name = "username_uniqueness", def = "{'username' : 1}") })
public class UserDocument {
    
    public final static String COLLECTION_NAME = UserDocument.class.getAnnotation(Document.class).collection();
    @Id
    private String username;
    private String password;
    private Date createdAt;
    private Role role;
    private Set<TaxEntry> taxEntries;
    private PersonalDetails personalDetails;
    
}


