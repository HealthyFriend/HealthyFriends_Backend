package server.healthyFriends.converter;

import server.healthyFriends.domain.entity.BodycompositionRecord;
import server.healthyFriends.web.dto.request.BodyInfoRequest;
import server.healthyFriends.web.dto.response.BodyInfoResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BodyInfoConverter {

    public static BodycompositionRecord toBodyInfo(BodyInfoRequest.CreateBodyInfoRequest req) {
        return BodycompositionRecord.builder()
                .body_fat_mass(req.getBody_fat_mass())
                .skeletal_muscle_mass(req.getSkeletal_muscle_mass())
                .weight(req.getWeight())
                .date(LocalDate.now())
                .build();
    }

    public static BodyInfoResponse.CreateBodyInfoResponse bodyInfoCreateResponse(Long bodyCompositionRecordId) {
        return BodyInfoResponse.CreateBodyInfoResponse.builder()
                .bodyInfoCompositionRecordId(bodyCompositionRecordId)
                .build();
    }

    public static BodyInfoResponse.DailyWeightChange dailyWeightChange(Optional<List<BigDecimal>> dailyWeightList) {
        return new BodyInfoResponse.DailyWeightChange(dailyWeightList);
    }


}
