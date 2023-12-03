package server.healthyFriends.apiPayload;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import server.healthyFriends.web.dto.response.objective.ObjectiveResponse;
import server.healthyFriends.domain.entity.Objective;

@Mapper(componentModel = "spring")
public interface EntityDtoMapper {
    EntityDtoMapper INSTANCE = Mappers.getMapper(EntityDtoMapper.class);

    ObjectiveResponse objectivetoDto(Objective objective);


}
