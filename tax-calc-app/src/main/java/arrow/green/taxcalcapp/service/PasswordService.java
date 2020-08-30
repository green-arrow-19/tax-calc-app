package arrow.green.taxcalcapp.service;

import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import arrow.green.taxcalcapp.exception.WeakPasswordException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nakulgoyal
 *         29/08/20
 **/

@Slf4j
@Service
public class PasswordService {
    
    private static final Pattern VALID_STRONG_PASSWORD_REGEX = Pattern.compile(
            "^.*(?=.{8,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\\$%^&+\\?!=.()\\+\\*,-\\./:;<>\\[\\]_{}~])" + ".*$");
    
    public boolean isValidStrongPassword(Long userId, String passwordUnderCheck) {
        if (StringUtils.isEmpty(passwordUnderCheck) || passwordUnderCheck.length() < 8
                || passwordUnderCheck.length() > 12) {
            throw new WeakPasswordException("Password can't have lesser than 8 characters or more than 12 characters");
        }
        
        if (!VALID_STRONG_PASSWORD_REGEX.matcher(passwordUnderCheck).matches()) {
            throw new WeakPasswordException(
                    "Password should have 1 upper case 1 special character and should be alphanumeric");
        }
        if (Objects.nonNull(userId)) {
            checkPasswordHistory(userId, passwordUnderCheck);
        }
        return true;
    }
    
    private void checkPasswordHistory(Long userId, String passwordUnderCheck) {
        // TODO : checkPasswordHistory
    }
}


