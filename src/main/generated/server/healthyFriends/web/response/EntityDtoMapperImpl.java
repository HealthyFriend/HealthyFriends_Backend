package server.healthyFriends.web.response;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import server.healthyFriends.apiPayload.EntityDtoMapper;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.web.dto.Objective.ObjectiveResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-03T19:02:26+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class EntityDtoMapperImpl implements EntityDtoMapper {

    @Override
    public ObjectiveResponse objectivetoDto(Objective objective) {
        if ( objective == null ) {
            return null;
        }

        ObjectiveResponse objectiveResponse = new ObjectiveResponse();

        objectiveResponse.setHead( objective.getHead() );
        objectiveResponse.setBody( objective.getBody() );
        objectiveResponse.setStatus( objective.getStatus() );

        return objectiveResponse;
    }
}
