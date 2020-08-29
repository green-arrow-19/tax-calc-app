package arrow.green.taxcalcapp.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import arrow.green.taxcalcapp.model.SignUpRequest;
import arrow.green.taxcalcapp.model.SignUpResponse;
import arrow.green.taxcalcapp.service.UserService;
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
    
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest,
            HttpServletResponse httpServletResponse) {
        log.info("SignUp request for user : {}", signUpRequest.getUsername());
        SignUpResponse signUpResponse = userService.signup(signUpRequest, httpServletResponse);
        log.info("SignUp request SUCCESSFUL for user : {}", signUpRequest.getUsername());
        return ResponseEntity.accepted().body(signUpResponse);
    }
}

