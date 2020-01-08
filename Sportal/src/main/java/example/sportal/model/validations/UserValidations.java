package example.sportal.model.validations;

import example.sportal.exceptions.BadRequestException;
import example.sportal.model.dto.RegisterUserDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidations {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._-]{3,}$");

    private static boolean validateEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.find();
    }

    private static boolean validatePasswordStrength(String password) {
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.find();
    }

    private static boolean validateConfirmPassword(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return true;
        }
        return false;
    }

    private static boolean validateUsername(String username) {
        Matcher matcher = USERNAME_PATTERN.matcher(username);
        return matcher.find();
    }


    public static void validateRegisterDto(RegisterUserDTO registerUserDto) throws BadRequestException {
        String username = registerUserDto.getUsername();
        String email = registerUserDto.getEmail();
        String password = registerUserDto.getPassword();
        String confirmPassword = registerUserDto.getConfirmPassword();

        if (!validateUsername(username)) {
            throw new BadRequestException("Username should be at least 3 chars and should contain only " +
                    "latin letters, digits, points, dashes and underscores");
        }

        if (!validateEmail(email)) {
            throw new BadRequestException("Invalid email!");
        }

        if (!validatePasswordStrength(password)) {
            throw new BadRequestException("Password should contain at least 8 chars including at least 1 digit, " +
                    "1 upper case, 1 lower case letter, 1 special char (@, #, %, $, ^)  and should NOT contain " +
                    "spaces or tabs!");
        }

        if (!validateConfirmPassword(password, confirmPassword)) {
            throw new BadRequestException("Confirm password should match password!");
        }
    }
}