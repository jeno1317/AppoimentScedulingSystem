package org.appointmentschedulingsystem.util.validation;

import io.micrometer.common.util.StringUtils;
import org.appointmentschedulingsystem.entity.*;
import org.appointmentschedulingsystem.util.exception.CustomException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component

public class Validation {

    public static void validateAppointment(ServiceProvider serviceProvider, Appointment appointment) {
        if (!isAppointmentSlotAvailable(serviceProvider, appointment)) {
            throw new CustomException("Regrettably, the appointment slot is not currently available.");
        }
        if (!checkBreakTime(serviceProvider, appointment)) {
            throw new CustomException("Apologies, this is my break time.");
        }
        if (!checkOffDay(serviceProvider, appointment)) {
            throw new CustomException("Apologies, but I'm unavailable on my off day.");
        }
        if (!isValidDate(appointment)) {
            throw new CustomException("Please enter valid Date and  month");
        }
        if (!isValidDate1(appointment)) {
            throw new CustomException("PLease enter check date and day !!");
        }
        validateAppointmentDetails(appointment);
        for (Appointment existingAppointment : serviceProvider.getAppointments()) {
            if (isTimeOverlap(existingAppointment, appointment)) {
                throw new CustomException(
                        "Apologies, the appointment slot has already been booked." +
                                " Assistance for a different time slot is available for your convenience.");
            }
        }

    }

    public void validateDate(OffDay offDay) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date oneDayPreviousDate = calendar.getTime();

        if (offDay.getFromDate().compareTo(oneDayPreviousDate) <= 0) {
            throw new CustomException("From date must be after today's date");
        }
        if (offDay.getToDate().before(offDay.getFromDate()) && !offDay.getToDate().equals(offDay.getFromDate())) {
            throw new CustomException("To date must be after or equal to from date");
        }
    }

    public static void validateAppointmentDetails(Appointment appointment) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date oneDayPreviousDate = calendar.getTime();

        if (appointment == null) {
            throw new CustomException("appointment is null");
        }
        // Check if the user is provided
        if (appointment.getUid() == null) {
            throw new CustomException("User ID is required");
        }
        // Check if the service provider is provided
        if (appointment.getSid() == null) {
            throw new CustomException("Service provider ID is required");
        }
        // Check if the appointment date is provided and is in the future
        if ((appointment.getDate() == null) || appointment.getDate().before(oneDayPreviousDate)) {
            throw new CustomException("Appointment date must be in the future");
        }
        // Check if the appointment time is provided
        if (StringUtils.isBlank(String.valueOf(appointment.getFromTime()))) {
            throw new CustomException("Appointment time is required");
        }
        if (StringUtils.isBlank(String.valueOf(appointment.getToTime()))) {
            throw new CustomException("Appointment time is required");
        }

    }

    public static boolean isAppointmentSlotAvailable(ServiceProvider serviceProvider, Appointment newAppointment) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date oneDayPreviousDate = calendar.getTime();

        if (newAppointment.getDate().before(oneDayPreviousDate)) {
            throw new CustomException("Appointment date must be in the future");
        }
        if (newAppointment.getFromTime().isAfter(newAppointment.getToTime())) {
            throw new CustomException("From time must be before to time");
        }

        LocalTime newAppointmentStartTime = newAppointment.getFromTime();
        LocalTime newAppointmentEndTime = newAppointment.getToTime();
        List<Appointment> bookedAppointments = serviceProvider.getAppointments();
        for (Appointment bookedAppointment : bookedAppointments) {
            Date bookedAppointmentDate = bookedAppointment.getDate();
            LocalTime bookedAppointmentStartTime = bookedAppointment.getFromTime();
            LocalTime bookedAppointmentEndTime = bookedAppointment.getToTime();

            if (oneDayPreviousDate.equals(bookedAppointmentDate)
                    && newAppointmentStartTime.isBefore(bookedAppointmentEndTime)
                    && newAppointmentEndTime.isAfter(bookedAppointmentStartTime)
            ) {
                return false;
            }
        }
        for (Appointment appointment : serviceProvider.getAppointments()) {
            if (newAppointment.getDate().equals(appointment.getDate())) {
                LocalTime newStart = newAppointment.getFromTime();
                LocalTime endTime = newAppointment.getToTime();
                LocalTime bookedStartTime = appointment.getFromTime();
                if (!(endTime.isBefore(bookedStartTime) || newStart.isAfter(bookedStartTime))) {
                    return false;

                }
            }
        }
        for (Appointment existingAppointment : serviceProvider.getAppointments()) {
            LocalTime bookedAppointmentStartTime = existingAppointment.getFromTime();
            LocalTime bookedAppointmentEndTime = existingAppointment.getToTime();
            if (newAppointmentStartTime.equals(bookedAppointmentStartTime)
                    && newAppointmentEndTime.equals(bookedAppointmentEndTime)
            ) {
                if (newAppointment.getDate().equals(existingAppointment.getDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkBreakTime(ServiceProvider serviceProvider, Appointment appointment) {
        boolean isAppointmentDayAvailable = serviceProvider.getAvailableWeekDay()
                .stream()
                .anyMatch(availableWeekDay1 -> availableWeekDay1.getDay().equals(appointment.getDay()));
        if (!isAppointmentDayAvailable) {
            throw new RuntimeException("Appointment day is not available");
        }
        for (AvailableWeekDay availableWeekDay : serviceProvider.getAvailableWeekDay()) {
            if (availableWeekDay.getDay().equals(appointment.getDay())) {
                for (BreakTime breakTime : availableWeekDay.getBreakTime()) {
                    LocalTime startBreakTime = breakTime.getFromTime1();
                    LocalTime endBreakTime = breakTime.getToTime1();
                    if (!(appointment.getToTime().isBefore(startBreakTime)
                            || appointment.getFromTime().isAfter(endBreakTime))
                    ) {
                        return false;
                    }
                }
                break;
            }
        }
        return true;

    }

    public static boolean checkOffDay(ServiceProvider serviceProvider, Appointment appointment) {
        Date appointmentDate = appointment.getDate();
        List<OffDay> offDays = serviceProvider.getOffDay();
        for (OffDay offDay : offDays) {
            Date fromDate = offDay.getFromDate();
            Date toDate = offDay.getToDate();
            if (!appointmentDate.before(fromDate) && !appointmentDate.after(toDate)) {
                serviceProvider.getAppointments()
                        .removeIf(
                                appointment1 -> !appointment1
                                        .getDate()
                                        .before(fromDate) && !appointment
                                        .getDate()
                                        .after(toDate));
                return false;
            }
        }
        return true;
    }

    private static boolean isTimeOverlap(Appointment existingAppointment, Appointment newAppointment) {
        if (!existingAppointment.getDate().equals(newAppointment.getDate())) {
            return false;
        }
        LocalTime existingFromTime = existingAppointment.getFromTime();
        LocalTime existingToTime = existingAppointment.getToTime();
        LocalTime newFromTime = newAppointment.getFromTime();
        LocalTime newToTime = newAppointment.getToTime();
        return (newFromTime.isBefore(existingToTime) && newToTime.isAfter(existingFromTime))
                || (existingFromTime.isBefore(newToTime) && existingToTime.isAfter(newFromTime)
        );
    }

    public static boolean isValidDate(Appointment appointment) {
        Date date = appointment.getDate();
        if (date == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month > 12) {
            return false;
        }
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        try {
            calendar.getTime();
            if (month == Calendar.FEBRUARY) {
                int year = calendar.get(Calendar.YEAR);
                boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                return day <= 29 && (day != 29 || isLeapYear);
            }
            return true;
        } catch (CustomException e) {
            return false;
        }
    }

    public static boolean isValidDate1(Appointment appointment) {
        Date date = appointment.getDate();
        if (date == null) {
            System.out.println("Appointment date is null");
            return false;
        }

        try {
            LocalDate appointmentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DayOfWeek appointmentDayOfWeek = appointmentDate.getDayOfWeek();
            String day = appointment.getDay();
            if (!day.equalsIgnoreCase(String.valueOf(appointmentDayOfWeek))) {
                System.out.println("Appointment date is not on an available weekday");
                return false;
            }
            return true;
        } catch (CustomException e) {
            System.out.println("Error processing the appointment date: " + e.getMessage());
            return false;
        }
    }

    public static void breakTimeCheck(BreakTime breakTime) {
        if (breakTime.getToTime1() == breakTime.getToTime1()) {
            throw new CustomException("Break ToTime And FromTime  Not Same !!");
        }
        if (breakTime.getToTime1().isAfter(breakTime.getFromTime1())) {
            throw new CustomException("Break FromTime Not After ToTime !!");
        }
        if (breakTime.getFromTime1().isBefore(breakTime.getToTime1())) {
            throw new CustomException("Break ToTime Not Before FromTime !!");
        }
    }
}