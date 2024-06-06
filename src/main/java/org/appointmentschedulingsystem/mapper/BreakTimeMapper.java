package org.appointmentschedulingsystem.mapper;

import org.appointmentschedulingsystem.config.MapStructConfig;
import org.appointmentschedulingsystem.dtos.BreakTimeDto;
import org.appointmentschedulingsystem.entity.BreakTime;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface BreakTimeMapper {

    BreakTimeMapper INSTANCE = Mappers.getMapper(BreakTimeMapper.class);

    BreakTimeDto breakTimeToBreakTimeDTO(BreakTime breakTime);
    BreakTime breakTimeDTOToBreakTime(BreakTimeDto breakTimeDTO);
    List<BreakTimeDto> breakTimeListToBreakTimeDTOList(List<BreakTime> breakTimes);
    List<BreakTime> breakTimeDTOListToBreakTimeList(List<BreakTimeDto> breakTimesDTO);

}
