package arrow.green.taxcalcapp.service;

import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import arrow.green.taxcalcapp.config.EmailServiceConfig;
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
import arrow.green.taxcalcapp.model.document.UserDocument;
import arrow.green.taxcalcapp.model.dto.UserDto;
import arrow.green.taxcalcapp.repository.UserDocumentRepository;
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
    private UserRepository userRepository;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserDocumentRepository userDocumentRepository;
    @Autowired
    private EmailServiceConfig emailServiceConfig;
    
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
        if (emailServiceConfig.getEnabled().equals(true)) {
            UserDocument userDocument = objectMapper.convertValue(userEntity, UserDocument.class);
            userDocument = userDocumentRepository.save(userDocument);
            sendVerificationMail(userDocument);
            return SignUpResponse.builder().userDto(objectMapper.convertValue(userEntity, UserDto.class))
                                 .status("VERIFICATION EMAIL TRIGGERED").build();
        }
        User savedUserEntity = userRepository.save(userEntity);
        return SignUpResponse.builder().userDto(objectMapper.convertValue(savedUserEntity, UserDto.class))
                             .status("SUCCESS").build();
    }
    
    private void sendVerificationMail(UserDocument userDocument) {
        // TODO : send mail for verification.
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
        authenticationService.authenticateUserForGetUser(authToken, user.get());
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
    
    public SignUpResponse signupVerified(String username, HttpServletResponse httpServletResponse) {
        Optional<UserDocument> userDocument = userDocumentRepository.findByUsername(username);
        if (userDocument.isEmpty()) {
            log.error("SignUp verified request FAILED for user : {}, user doesn't exist.", username);
            throw new UserNotFoundException("User : " + username + ", doesn't exists");
        }
        User user = objectMapper.convertValue(userDocument.get(), User.class);
        user = userRepository.save(user);
        userDocumentRepository.deleteById(username);
        return SignUpResponse.builder().userDto(objectMapper.convertValue(user, UserDto.class))
                             .status("REGISTERED SUCCESSFULLY").build();
    }
}


