package org.appointmentschedulingsystem.mapper;

import org.appointmentschedulingsystem.config.MapStructConfig;
import org.appointmentschedulingsystem.dtos.OffDayDto;
import org.appointmentschedulingsystem.entity.OffDay;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface OffDayMapper {

    OffDayMapper INSTANCE = Mappers.getMapper(OffDayMapper.class);

   // @Mapping(target = "type",source = "type")
    OffDayDto OffDayToOffDayDTO(OffDay offDay);
    OffDay OffDayDTOToOffDay(OffDayDto offDayDTO);
    List<OffDayDto> OffDaysToOffDayDTOs(List<OffDay> offDays);
    List<OffDay>OffDayDTOsToOffDays(List<OffDayDto> offDayDTOs);

}
