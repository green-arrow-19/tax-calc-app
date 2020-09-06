package arrow.green.taxcalcapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import arrow.green.taxcalcapp.exception.NotAuthorizedException;
import arrow.green.taxcalcapp.exception.UserNotFoundException;
import arrow.green.taxcalcapp.model.Role;
import arrow.green.taxcalcapp.model.User;
import arrow.green.taxcalcapp.repository.UserRepository;
import arrow.green.taxcalcapp.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nakulgoyal
 *         30/08/20
 **/

@Slf4j
@Service
public class AuthenticationService {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    public void authenticateUserForGetUser(String authToken, User user) {
        if (!Role.ADMIN.equals(user.getRole())) {
            String authUserName = jwtUtil.extractUsername(authToken.substring(7));
            if (!user.getUsername().equals(authUserName)) {
                log.error("Not authorized to get details of other user");
                throw new NotAuthorizedException("Not authorized to get details of other user");
            }
        }
    }
    
    public User extractUser(String authToken) {
        log.info("Extracting user from authToken");
        String authUserName = jwtUtil.extractUsername(authToken.substring(7));
        Optional<User> user = userRepository.findByUsername(authUserName);
        if(user.isEmpty()) {
            log.error("User : {} doesn't exist", authUserName);
            throw new UserNotFoundException("User : " + authUserName + ", doesn't exists");
        }
        return user.get();
    }
}


