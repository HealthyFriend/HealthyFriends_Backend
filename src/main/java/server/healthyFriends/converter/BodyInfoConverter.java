package server.healthyFriends.converter;

import jakarta.persistence.Tuple;
import server.healthyFriends.domain.entity.BodycompositionRecord;
import server.healthyFriends.web.dto.request.BodyInfoRequest;
import server.healthyFriends.web.dto.response.BodyInfoResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public static BodyInfoResponse.DailyWeightChange dailyWeightChange(List<Tuple> tupleList) {
        List<BodyInfoResponse.WeightChange> weightChangeList = tupleList.stream()
                .map(tuple -> new BodyInfoResponse.WeightChange(
                        tuple.get(0, LocalDate.class),
                        tuple.get(1, BigDecimal.class)))
                .toList();
        return new BodyInfoResponse.DailyWeightChange(Optional.of(weightChangeList));
    }

    public static BodyInfoResponse.WeightChange weightChange(Object[] result) {
        return new BodyInfoResponse.WeightChange((LocalDate) result[1], (BigDecimal) result[0]);
    }

    public static BodyInfoResponse.DailyWeightChange convertToDailyWeightChange(List<Object[]> weightChangeList) {
        List<BodyInfoResponse.WeightChange> weightChanges = weightChangeList.stream()
                .map(BodyInfoConverter::weightChange)
                .collect(Collectors.toList());

        return new BodyInfoResponse.DailyWeightChange(Optional.of(weightChanges));
    }

}
