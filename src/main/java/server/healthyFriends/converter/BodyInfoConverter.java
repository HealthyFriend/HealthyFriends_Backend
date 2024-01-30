package server.healthyFriends.converter;

import jakarta.persistence.Tuple;
import org.springframework.http.StreamingHttpOutputMessage;
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

    public static BodyInfoResponse.UpdateBodyInfoResponse bodyInfoUpdateResponse(BodycompositionRecord bodycompositionRecord) {
        return BodyInfoResponse.UpdateBodyInfoResponse.builder()
                .body_fat_mass(bodycompositionRecord.getBody_fat_mass())
                .skeletal_muscle_mass(bodycompositionRecord.getSkeletal_muscle_mass())
                .weight(bodycompositionRecord.getWeight())
                .edit_day(bodycompositionRecord.getDate())
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
                .toList();

        return new BodyInfoResponse.DailyWeightChange(Optional.of(weightChanges));
    }

    public static BodyInfoResponse.MuscleChange muscleChange(Object[] result) {
        return new BodyInfoResponse.MuscleChange((LocalDate) result[1], (BigDecimal) result[0]);
    }

    public static BodyInfoResponse.DailyMuscleChange convertToDailyMuscleChange(List<Object[]> muscleChangeList) {
        List<BodyInfoResponse.MuscleChange> muscleChanges = muscleChangeList.stream()
                .map(BodyInfoConverter::muscleChange)
                .toList();

        return new BodyInfoResponse.DailyMuscleChange(Optional.of(muscleChanges));
    }

    public static BodyInfoResponse.FatChange fatChange(Object[] result) {
        return new BodyInfoResponse.FatChange((LocalDate) result[1], (BigDecimal) result[0]);
    }

    public static BodyInfoResponse.DailyFatChange convertToDailyFatChange(List<Object[]> fatChangeList) {
        List<BodyInfoResponse.FatChange> fatChanges = fatChangeList.stream()
                .map(BodyInfoConverter::fatChange)
                .toList();

        return new BodyInfoResponse.DailyFatChange(Optional.of(fatChanges));
    }

    public static BodyInfoResponse.BmiChange bmiChange(Object[] result) {
        return new BodyInfoResponse.BmiChange((LocalDate) result[1], (BigDecimal) result[0]);
    }

    public static BodyInfoResponse.DailyBmiChange convertToDailyBmiChange(List<Object[]> bmiChangeList) {
        List<BodyInfoResponse.BmiChange> bmiChanges = bmiChangeList.stream()
                .map(BodyInfoConverter::bmiChange)
                .toList();

        return new BodyInfoResponse.DailyBmiChange(Optional.of(bmiChanges));
    }
}
