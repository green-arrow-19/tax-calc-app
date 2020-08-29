package arrow.green.taxcalcapp.service;

import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import arrow.green.taxcalcapp.exception.UserAlreadyExistException;
import arrow.green.taxcalcapp.exception.WeakPasswordException;
import arrow.green.taxcalcapp.model.PersonalDetails;
import arrow.green.taxcalcapp.model.SignUpRequest;
import arrow.green.taxcalcapp.model.SignUpResponse;
import arrow.green.taxcalcapp.model.User;
import arrow.green.taxcalcapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nakulgoyal
 *         28/08/20
 **/

@Slf4j
@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    PasswordService passwordService;
    
    public SignUpResponse signup(SignUpRequest signUpRequest, HttpServletResponse httpServletResponse) {
        Optional<User> user = userRepository.findByUsername(signUpRequest.getUsername());
        if (user.isPresent()) {
            log.error("SignUp request FAILED for user : {}, user already present.", signUpRequest.getUsername());
            throw new UserAlreadyExistException("SignUp request FAILED for user : " + signUpRequest.getUsername()
                                                        + ", user already present.");
        }
        if (!passwordService.isValidStrongPassword(null, signUpRequest.getPassword())) {
            log.error("SignUp request FAILED for user : {}, password validation failed.", signUpRequest.getUsername());
            throw new WeakPasswordException("SignUp request FAILED for user : " + signUpRequest.getUsername() +
                                                    ", password validation failed.");
        }
        log.info("SignUp request for user : {}, Saving user", signUpRequest.getUsername());
        User userEntity = generateUser(signUpRequest);
        User savedUserEntity = userRepository.save(userEntity);
        return SignUpResponse.builder()
                             .username(savedUserEntity.getUsername())
                             .status("SUCCESS")
                             .personalDetails(savedUserEntity.getPersonalDetails())
                             .build();
    }
    
    private User generateUser(SignUpRequest signUpRequest) {
        User userEntity = new User();
        userEntity.setUsername(signUpRequest.getUsername());
        userEntity.setPassword(signUpRequest.getUsername());
        userEntity.setPersonalDetails(PersonalDetails.builder()
                                                     .firstName(signUpRequest.getFirstName())
                                                     .lastName(signUpRequest.getLastName())
                                                     .middleName(signUpRequest.getMiddleName())
                                                     .prefix(signUpRequest.getPrefix())
                                                     .build());
        return userEntity;
    }
}


