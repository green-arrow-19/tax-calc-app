package arrow.green.taxcalcapp.service;

import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import arrow.green.taxcalcapp.exception.NotAuthorizedException;
import arrow.green.taxcalcapp.exception.UserAlreadyExistException;
import arrow.green.taxcalcapp.exception.UserNotFoundException;
import arrow.green.taxcalcapp.exception.WeakPasswordException;
import arrow.green.taxcalcapp.model.PersonalDetails;
import arrow.green.taxcalcapp.model.Role;
import arrow.green.taxcalcapp.model.SignInRequest;
import arrow.green.taxcalcapp.model.SignInResponse;
import arrow.green.taxcalcapp.model.SignUpRequest;
import arrow.green.taxcalcapp.model.SignUpResponse;
import arrow.green.taxcalcapp.model.User;
import arrow.green.taxcalcapp.model.dto.UserDto;
import arrow.green.taxcalcapp.repository.UserRepository;
import arrow.green.taxcalcapp.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nakulgoyal
 *         28/08/20
 **/

@Slf4j
@Service
public class UserService {
    
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordService passwordService;
    @Autowired
    JwtUtil jwtUtil;
    
    public SignUpResponse signup(SignUpRequest signUpRequest, HttpServletResponse httpServletResponse) {
        Optional<User> user = userRepository.findByUsername(signUpRequest.getUsername());
        if (user.isPresent()) {
            log.error("SignUp request FAILED for user : {}, user already present.", signUpRequest.getUsername());
            throw new UserAlreadyExistException(
                    "SignUp request FAILED for user : " + signUpRequest.getUsername() + ", user already present.");
        }
        if (!passwordService.isValidStrongPassword(null, signUpRequest.getPassword())) {
            log.error("SignUp request FAILED for user : {}, password validation failed.", signUpRequest.getUsername());
            throw new WeakPasswordException("SignUp request FAILED for user : " + signUpRequest.getUsername()
                                                    + ", password validation failed.");
        }
        log.info("SignUp request for user : {}, Saving user", signUpRequest.getUsername());
        User userEntity = generateUser(signUpRequest);
        User savedUserEntity = userRepository.save(userEntity);
        return SignUpResponse.builder().userDto(objectMapper.convertValue(userEntity, UserDto.class)).status("SUCCESS")
                             .build();
    }
    
    private User generateUser(SignUpRequest signUpRequest) {
        User userEntity = new User();
        userEntity.setRole(Role.USER);
        userEntity.setUsername(signUpRequest.getUsername());
        userEntity.setPassword(signUpRequest.getPassword());
        userEntity.setPersonalDetails(
                PersonalDetails.builder().firstName(signUpRequest.getFirstName()).lastName(signUpRequest.getLastName())
                               .middleName(signUpRequest.getMiddleName()).prefix(signUpRequest.getPrefix()).build());
        return userEntity;
    }
    
    public UserDto getUser(String username, String authToken) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.error("Getting user details, user : {} doesn't exist", username);
            throw new UserNotFoundException("User : " + username + ", doesn't exists");
        }
        if (!Role.ADMIN.equals(user.get().getRole())) {
            String authUserName = jwtUtil.extractUsername(authToken.substring(7));
            if (!username.equals(authUserName)) {
                log.error("Not authorized to get details of other user");
                throw new NotAuthorizedException("Not authorized to get details of other user");
            }
        }
        return objectMapper.convertValue(user.get(), UserDto.class);
    }
    
    public SignInResponse signin(SignInRequest signInRequest, HttpServletResponse httpServletResponse) {
        Optional<User> user = userRepository.findByUsername(signInRequest.getUsername());
        if (user.isEmpty()) {
            log.error("SignIn request FAILED for user : {}, user doesn't exist.", signInRequest.getUsername());
            throw new UserNotFoundException("User : " + signInRequest.getUsername() + ", doesn't exists");
        }
        if (!(user.get().getPassword().equals(signInRequest.getPassword()))) {
            log.error("SignIn request FAILED for user : {}, incorrect password.", signInRequest.getUsername());
            throw new IllegalArgumentException("Incorrect Password");
        }
        log.info("Generating token for user : {}", signInRequest.getUsername());
        String token = jwtUtil.generateToken(user.get());
        httpServletResponse.setHeader("Authorization", "Bearer " + token);
        return SignInResponse.builder().userDto(objectMapper.convertValue(user.get(), UserDto.class)).status("SUCCESS")
                             .jwtToken(token).build();
        
    }
}


