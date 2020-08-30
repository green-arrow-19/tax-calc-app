package arrow.green.taxcalcapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import arrow.green.taxcalcapp.exception.NotAuthorizedException;
import arrow.green.taxcalcapp.model.Role;
import arrow.green.taxcalcapp.model.User;
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
    JwtUtil jwtUtil;
    
    public void authenticateUser(String authToken, User user) {
        if (!Role.ADMIN.equals(user.getRole())) {
            String authUserName = jwtUtil.extractUsername(authToken.substring(7));
            if (!user.getUsername().equals(authUserName)) {
                log.error("Not authorized to get details of other user");
                throw new NotAuthorizedException("Not authorized to get details of other user");
            }
        }
    }
}


