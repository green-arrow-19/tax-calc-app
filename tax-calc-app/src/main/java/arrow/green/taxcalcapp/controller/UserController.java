package arrow.green.taxcalcapp.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import arrow.green.taxcalcapp.model.HeaderConstants;
import arrow.green.taxcalcapp.model.SignInRequest;
import arrow.green.taxcalcapp.model.SignInResponse;
import arrow.green.taxcalcapp.model.SignUpRequest;
import arrow.green.taxcalcapp.model.SignUpResponse;
import arrow.green.taxcalcapp.model.dto.UserDto;
import arrow.green.taxcalcapp.service.UserService;
import arrow.green.taxcalcapp.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nakulgoyal
 *         28/08/20
 **/

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    UserService userService;

    
    @GetMapping("")
    public ResponseEntity<UserDto> getUser(@RequestParam String username,
            @RequestHeader(HeaderConstants.AUTHORIZATION) String auth) {
        if (StringUtils.isEmpty(username)) {
            log.error("Getting user details, username can't be empty or null");
            throw new IllegalArgumentException("Username can't be empty or null");
        }
        log.info("Getting user details for user : {}", username);
        UserDto userDto = userService.getUser(username, auth);
        log.info("Getting user details SUCCESS for user : {}", username);
        return ResponseEntity.ok().body(userDto);
    }
    
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest,
            HttpServletResponse httpServletResponse) {
        log.info("SignUp request for user : {}", signUpRequest.getUsername());
        SignUpResponse signUpResponse = userService.signup(signUpRequest, httpServletResponse);
        log.info("SignUp request SUCCESSFUL for user : {}", signUpRequest.getUsername());
        return ResponseEntity.accepted().body(signUpResponse);
    }
    
    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signin(@Valid @RequestBody SignInRequest signInRequest,
            HttpServletResponse httpServletResponse) {
        log.info("SignIn request for user : {}", signInRequest.getUsername());
        SignInResponse signInResponse = userService.signin(signInRequest, httpServletResponse);
        log.info("SignIn request SUCCESSFUL for user : {}", signInRequest.getUsername());
        return ResponseEntity.ok().body(signInResponse);
    }
}

