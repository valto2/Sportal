package example.sportal.validations;

public class UserValidations {

        private static final String USERNAME_PATTERN = "([A-Za-z0-9_]+).{4,45}";
        private static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{6,225})";


        //validate username
        public static boolean isUsernameValid(String username) {
            if (username.isEmpty() || username.trim().isEmpty()){
                return false;
            } else {
                return username.matches(USERNAME_PATTERN);
            }
        }

        //validate eMail
        public static boolean isEMailValid(String email){
            if (email.isEmpty() || email.trim().isEmpty()){
                return false;
            } else {
                return email.matches(EMAIL_PATTERN);
            }
        }

        //validate password
        public static boolean isPasswordValid(String password){
            if (password.isEmpty() || password.trim().isEmpty()){
                return false;
            } else {
                return password.matches(PASSWORD_PATTERN);
            }
        }
    }

