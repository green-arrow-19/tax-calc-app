package arrow.green.taxcalcapp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import arrow.green.taxcalcapp.model.document.UserDocument;

/**
 * @author nakulgoyal
 *         12/09/20
 **/

public interface UserDocumentRepository extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByUsername(String username);
    
}


