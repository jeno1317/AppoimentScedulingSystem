package org.appointmentschedulingsystem.services;

import org.appointmentschedulingsystem.dtos.*;
import org.appointmentschedulingsystem.util.enums.HolidayType;

import java.util.List;

public interface ServiceProviderService {
    List<ServiceProviderDto> getProfessional();
    ServiceProviderDto addServiceProvider(ServiceProviderDto serviceProviderDTO);
    List<ServiceProviderDto> serviceProviderById(String id);
    void deleteProfession(String id);
    ServiceProviderDto updateServiceProviderDetails(String email, ServiceProviderDto serviceProviderDTO);
    AvailableWeekDayDto addSingleAvailableWeekDay(String id, AvailableWeekDayDto availableWeekDayDTO);
    List<AvailableWeekDayDto> addDayAndTime(String id, List<AvailableWeekDayDto> availableWeekDayDTOS);
    void deleteDayAndTime(String id, String day);
    AvailableWeekDayDto updateDayAndTime(String id, AvailableWeekDayDto availableWeekDayDTO);
    List<AvailableWeekDayDto> getAvailableWeekDayDetail(String id);
    OffDayDto addOffDay(String id, OffDayDto offDayDTO);
    List<OffDayDto> addOffDay(String id, List<OffDayDto> offDayDto);
    List<OffDayDto> updateOffDay(String id, List<OffDayDto> offDayDTOList);
    OffDayDto updateOffDay(String id, OffDayDto offDayDTO);
    void deleteOffDay(String id, HolidayType type);
    List<OffDayDto> getOffDayDetails(String id);
    LocationDto addProfessionalLocation(String id, LocationDto locationDTO);
    LocationDto updateProfessionLocation(String id, LocationDto locationDTO);
    List<BreakTimeDto> addBreakTime(String id, String day, List<BreakTimeDto> breakTimeDTO);
    void deleteBreakTimes(String id, String title, String day);
    List<BreakTimeDto> updateBreakTime(String id, String day, List<BreakTimeDto> breakTimeDTOS);
    void deleteLocation(String id, String type);


}
