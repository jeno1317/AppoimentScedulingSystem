package org.appointmentschedulingsystem.util.validation;

import org.appointmentschedulingsystem.entity.User;
import org.appointmentschedulingsystem.util.exception.CustomException;
import java.util.regex.Pattern;

public class UserValidation {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PHONE_NUMBER_REGEX = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&amp;+=])(?=\\S+$).{8,}$";

    public static void userValidation(User user) {
        if (!validatePhoneNumber(user)) {
            throw new CustomException("Please enter 10 digit number");
        }
        if (!validateEmail(user)) {
            throw new CustomException("Please enter valid email address. example@example.com !!");
        }
        if (!validatePassword(user)) {
            throw new CustomException("Password must contain at least 8 characters," +
                    " including at least one uppercase letter, one lowercase letter," +
                    " one digit, and one special character");
        }
    }

    private static boolean validateEmail(User user) {
        String email = user.getEmail();
        if (email == null || email.isEmpty()) {
            throw new CustomException("Email Address cannot be null or empty!");
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        if (!pattern.matcher(email).matches()) {
            throw new CustomException("Please enter a valid email address!");
        }
        return true;
    }

    private static boolean validatePassword(User user) {
        String password = user.getPassword();
        if (password == null || password.isEmpty()) {
            throw new CustomException("Password cannot be null or empty!");
        }
        if (!password.matches(PASSWORD_REGEX)) {
            throw new CustomException("Password must contain at least 8 characters," +
                    " including at least one uppercase letter, one lowercase letter," +
                    " one digit, and one special character, and must not contain any spaces.");
        }
        return true;
    }

    private static boolean validatePhoneNumber(User user) {
        if (user == null) {
            throw new CustomException("User details cannot be null!");
        }
        String phoneNumber = user.getPhoneNumber();
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new CustomException("Phone number cannot be null or empty!");
        }
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        if (!pattern.matcher(phoneNumber).matches()) {
            throw new CustomException("Please enter a valid phone number!");
        }
        return true;
    }

}

