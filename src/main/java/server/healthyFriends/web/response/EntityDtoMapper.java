package server.healthyFriends.web.response;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import server.healthyFriends.domain.dto.ObjectiveResponse;
import server.healthyFriends.domain.entity.Objective;

import javax.persistence.Entity;

@Mapper(componentModel = "spring")
public interface EntityDtoMapper {
    EntityDtoMapper INSTANCE = Mappers.getMapper(EntityDtoMapper.class);

    ObjectiveResponse objectivetoDto(Objective objective);


}
