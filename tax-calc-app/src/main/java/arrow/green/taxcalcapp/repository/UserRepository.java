package arrow.green.taxcalcapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import arrow.green.taxcalcapp.model.User;

/**
 * @author nakulgoyal
 *         28/08/20
 **/
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}


