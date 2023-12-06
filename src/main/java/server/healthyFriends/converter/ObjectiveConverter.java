package server.healthyFriends.converter;

import org.springframework.data.domain.Page;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.enums.Role;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.ObjectiveResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectiveConverter {

    public static ObjectiveResponse.CreateObjectiveResponse createObjectiveResponse(Long objectiveId) {
        return ObjectiveResponse.CreateObjectiveResponse.builder()
                .objectiveId(objectiveId)
                .build();
    }

    public static ObjectiveResponse.CreateObjectiveResultDTO toCreateObjective(Objective objective) {
        return ObjectiveResponse.CreateObjectiveResultDTO.builder()
                .objectiveId(objective.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ObjectiveResponse.SingleObjectiveResponse SingleObject(Objective existingObject) {
        return ObjectiveResponse.SingleObjectiveResponse.builder()
                .body(existingObject.getBody())
                .head(existingObject.getHead())
                .end_Day(existingObject.getEnd_day())
                .start_Day(existingObject.getStart_day())
                .build();
    }

    public static ObjectiveResponse.ListObjectiveResponse ListObject(Page<Objective> objectList) {
        List<ObjectiveResponse.SingleObjectiveResponse> singleObjectiveResponseList = objectList.stream()
                .map(ObjectiveConverter::SingleObject)
                .collect(Collectors.toList());

        return ObjectiveResponse.ListObjectiveResponse.builder()
                .isLast(objectList.isLast())
                .isFirst(objectList.isFirst())
                .totalPage(objectList.getTotalPages())
                .totalElements(objectList.getTotalElements())
                .listSize(objectList.getSize())
                .objectiveList(singleObjectiveResponseList)
                .build();
    }

}
