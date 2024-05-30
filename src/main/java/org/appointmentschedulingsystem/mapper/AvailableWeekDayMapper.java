package org.appointmentschedulingsystem.mapper;

import org.appointmentschedulingsystem.entity.AvailableWeekDay;
import org.appointmentschedulingsystem.dtos.AvailableWeekDayDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper

public interface AvailableWeekDayMapper {

    AvailableWeekDayMapper INSTANCE = Mappers.getMapper(AvailableWeekDayMapper.class);

   // @Mapping(source = "BreakTimeDTO",target = "BreakTime")
    AvailableWeekDayDto mapToDTO(AvailableWeekDay availableWeekDay);
    AvailableWeekDay DTOToMap(AvailableWeekDayDto availableWeekDayDTO);
    List<AvailableWeekDayDto> mapToDTO(List<AvailableWeekDay> availableWeekDays);
    List<AvailableWeekDay> mapToList(List<AvailableWeekDayDto> availableWeekDayDTOList);

}
