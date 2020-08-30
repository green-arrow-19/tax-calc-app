package arrow.green.taxcalcapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import arrow.green.taxcalcapp.exception.UserNotFoundException;
import arrow.green.taxcalcapp.model.User;
import arrow.green.taxcalcapp.repository.UserRepository;

/**
 * @author nakulgoyal
 *         29/08/20
 **/

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User : " + username + ", doesn't exists");
        }
        return user.get();
        
    }
}


