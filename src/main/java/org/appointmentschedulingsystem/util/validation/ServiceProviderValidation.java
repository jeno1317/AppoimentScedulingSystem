package org.appointmentschedulingsystem.util.validation;

import org.appointmentschedulingsystem.entity.AvailableWeekDay;
import org.appointmentschedulingsystem.entity.BreakTime;
import org.appointmentschedulingsystem.entity.OffDay;
import org.appointmentschedulingsystem.entity.ServiceProvider;
import org.appointmentschedulingsystem.util.exception.Exception;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class ServiceProviderValidation {

    private static final String PHONE_NUMBER_REGEX = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&amp;+=])(?=\\S+$).{8,}$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static void serviceProviderValidation(ServiceProvider serviceProvider) {
        if (serviceProvider == null) {
            throw new Exception("Service provider details cannot be null!");
        }

        String phoneNumber = serviceProvider.getProfessionContactNumber();
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new Exception("Professional contact number cannot be null or empty!");
        }

        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        if (!pattern.matcher(phoneNumber).matches()) {
            throw new Exception("Please enter a valid phone number!");
        }

        if (!validateEmail(serviceProvider)) {
            throw new Exception("Please enter valid email address. example@example.com !!");
        }
        if (!validatePassword(serviceProvider)) {
            throw new Exception("Password must contain at least 8 characters," +
                    " including at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character");
        }

    }

    public static void checkAvailableWeekDay(List<AvailableWeekDay> availableWeekDay) {
        for (AvailableWeekDay availableWeekDay1 : availableWeekDay) {
            LocalTime fromTime = availableWeekDay1.getFromTime();
            LocalTime toTime = availableWeekDay1.getToTime();

            if (fromTime.equals(toTime)) {
                throw new Exception("FromTime and ToTime cannot be the same");
            }

            if (fromTime.isAfter(toTime)) {
                throw new Exception("FromTime must be before ToTime");
            }
        }
    }

    public static void checkOffDay(List<OffDay> offDays) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date oneDayPreviousDate = calendar.getTime();
        for (OffDay offDay : offDays) {
            if (offDay.getFromDate().compareTo(oneDayPreviousDate) <= 0) {
                throw new Exception("From date must be after today's date");
            }

            if (offDay.getToDate().before(offDay.getFromDate()) && !offDay.getToDate().equals(offDay.getFromDate())) {
                throw new Exception("To date must be after or equal to from date");
            }
        }

    }

    public static void checkBreakTimes(List<AvailableWeekDay> availableWeekDays) {
        for (AvailableWeekDay availableWeekDay : availableWeekDays) {
            List<BreakTime> breakTimes = availableWeekDay.getBreakTime();
            if (breakTimes != null) {
                for (BreakTime breakTime : breakTimes) {
                    validateBreakTime(breakTime);
                }
            }
        }
    }

    private static void validateBreakTime(BreakTime breakTime) {
        if (breakTime.getToTime1().equals(breakTime.getFromTime1())) {
            throw new Exception("Break ToTime and FromTime cannot be the same!");
        }
        if (breakTime.getFromTime1().isAfter(breakTime.getToTime1())) {
            throw new Exception("Break FromTime must be before ToTime!");
        }
    }
    private static boolean validatePassword(ServiceProvider serviceProvider) {
        String password = serviceProvider.getPassword();
        if (password == null || password.isEmpty()) {
            throw new Exception("Password cannot be null or empty!");
        }
        if (!password.matches(PASSWORD_REGEX)) {
            throw new Exception("Password must contain at least 8 characters, " +
                    "including at least one uppercase letter, one lowercase letter," +
                    " one digit, and one special character, and must not contain any spaces.");
        }
        return true;
    }
    private static boolean validateEmail(ServiceProvider serviceProvider) {
        String email = serviceProvider.getEmail();
        if (email == null || email.isEmpty()) {
            throw new Exception("Email Address cannot be null or empty!");
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        if (!pattern.matcher(email).matches()) {
            throw new Exception("Please enter a valid email address!");
        }
        return true;
    }

}