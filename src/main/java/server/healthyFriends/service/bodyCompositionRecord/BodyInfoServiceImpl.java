package server.healthyFriends.service.bodyCompositionRecord;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import server.healthyFriends.converter.BodyInfoConverter;
import server.healthyFriends.domain.entity.BodycompositionRecord;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.entity.mapping.FriendMapping;
import server.healthyFriends.repository.BodyInfoRepository;
import server.healthyFriends.repository.FriendRepository;
import server.healthyFriends.repository.UserRepository;
import server.healthyFriends.service.bodyCompositionRecord.BodyInfoService;
import server.healthyFriends.web.dto.request.BodyInfoRequest;
import server.healthyFriends.web.dto.response.BodyInfoResponse;
import server.healthyFriends.web.dto.response.UserResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BodyInfoServiceImpl implements BodyInfoService {

    private final UserRepository userRepository;
    private final BodyInfoRepository bodyInfoRepository;
    private final FriendRepository friendRepository;

    public BodyInfoResponse.CreateBodyInfoResponse createBodyInfo(Long userId, BodyInfoRequest.CreateBodyInfoRequest req) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));

        if (bodyInfoRepository.existsByuserIdAndDate(userId, LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"오늘 일자의 기록이 있습니다.");
        }

        BodycompositionRecord bodycompositionRecord = BodyInfoConverter.toBodyInfo(req);

        bodycompositionRecord.setUser(user);

        if(user.getHeight()!=null && req.getWeight()!=null) {

            BigDecimal kg = req.getWeight();
            BigDecimal m = user.getHeight().divide(new BigDecimal(100), 3, RoundingMode.HALF_UP);

            BigDecimal bmi = kg.divide(m.pow(2), 2, RoundingMode.HALF_UP);

            bodycompositionRecord.setBmi(bmi);
        }

        bodyInfoRepository.save(bodycompositionRecord);

        return BodyInfoConverter.bodyInfoCreateResponse(bodycompositionRecord.getId());
    }

    public BodyInfoResponse.UpdateBodyInfoResponse updateBodyInfo(Long bodyCompositionRecordId, BodyInfoRequest.UpdateBodyInfoRequest req) {

        BodycompositionRecord bodycompositionRecord=bodyInfoRepository.findById(bodyCompositionRecordId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 체성분 기록이 없습니다."));

        if(req.getEdit_day()==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정하려는 날짜는 필수 입력입니다.");
        }

        if(bodyInfoRepository.existsByuserIdAndDate(bodycompositionRecord.getUser().getId(),req.getEdit_day()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"수정하려는 날짜에 이미 기록이 존재합니다.");
        }

        else {
            bodycompositionRecord.setDate(req.getEdit_day());

            if (req.getBody_fat_mass() != null) {
                bodycompositionRecord.setBody_fat_mass(req.getBody_fat_mass());
            }

            if(req.getWeight()!=null) {
                bodycompositionRecord.setWeight(req.getWeight());

                User user = bodycompositionRecord.getUser();

                BigDecimal kg = req.getWeight();
                BigDecimal m = user.getHeight().divide(new BigDecimal(100),3,RoundingMode.HALF_UP);

                BigDecimal bmi = kg.divide(m.pow(2), 2, RoundingMode.HALF_UP);

                bodycompositionRecord.setBmi(bmi);
            }

            if(req.getSkeletal_muscle_mass()!=null) {
                bodycompositionRecord.setSkeletal_muscle_mass(req.getSkeletal_muscle_mass());
            }
        }

        bodyInfoRepository.save(bodycompositionRecord);

        return BodyInfoConverter.bodyInfoUpdateResponse(bodycompositionRecord);
    }


    /**
     * 나의 체성분 기록 조회
     */

    public Optional<BodyInfoResponse.DailyWeightChange> getDailyWeightChange(Long userId) {

        existsUser(userId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestWeightRecordDate(userId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        else {
            LocalDate firstRecordDate = (earliestDate.isBefore(LocalDate.now().minusYears(1)))
                    ? LocalDate.now().minusYears(1)
                    : earliestDate;

            List<Object[]> dailyWeightList = bodyInfoRepository.findDailyWeightChange(userId, firstRecordDate);

            return Optional.of(BodyInfoConverter.convertToDailyWeightChange(dailyWeightList));
        }
    }

    public Optional<BodyInfoResponse.DailyMuscleChange> getDailyMuscleChange(Long userId) {

        existsUser(userId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestMuscleRecordDate(userId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        else {
            LocalDate firstRecordDate = (earliestDate.isBefore(LocalDate.now().minusYears(1)))
                    ? LocalDate.now().minusYears(1)
                    : earliestDate;

            List<Object[]> dailyMuscleList = bodyInfoRepository.findDailyMuscleChange(userId, firstRecordDate);

            return Optional.of(BodyInfoConverter.convertToDailyMuscleChange(dailyMuscleList));
        }

    }

    public Optional<BodyInfoResponse.DailyFatChange> getDailyFatChange(Long userId) {

        existsUser(userId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestFatRecordDate(userId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        else {
            LocalDate firstRecordDate = (earliestDate.isBefore(LocalDate.now().minusYears(1)))
                    ? LocalDate.now().minusYears(1)
                    : earliestDate;

            List<Object[]> dailyFatList = bodyInfoRepository.findDailyFatChange(userId, firstRecordDate);

            return Optional.of(BodyInfoConverter.convertToDailyFatChange(dailyFatList));
        }

    }

    public Optional<BodyInfoResponse.DailyBmiChange> getDailyBmiChange(Long userId) {

        existsUser(userId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestBmiRecordDate(userId);

        if (earliestDate == null) {
            return Optional.empty();
        } else {
            LocalDate firstRecordDate = (earliestDate.isBefore(LocalDate.now().minusYears(1)))
                    ? LocalDate.now().minusYears(1)
                    : earliestDate;

            List<Object[]> dailyBmiList = bodyInfoRepository.findDailyBmiChange(userId, firstRecordDate);

            return Optional.of(BodyInfoConverter.convertToDailyBmiChange(dailyBmiList));
        }
    }

    public Optional<BodyInfoResponse.MonthlyWeightChange> getMonthlyWeightChange(Long userId) {

        existsUser(userId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestWeightRecordDate(userId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        List<Object[]> monthlyWeightList = bodyInfoRepository.findMonthlyWeightChange(userId, earliestDate);

        List<BodyInfoResponse.WeightChange> weightChanges = monthlyWeightList.stream()
                .map(result -> {
                    Integer year = (Integer) result[0];
                    Integer month = (Integer) result[1];
                    BigDecimal averageWeight = null;
                    if (result[2] instanceof Double) {
                        averageWeight = BigDecimal.valueOf((Double) result[2]);
                    } else if (result[2] instanceof BigDecimal) {
                        averageWeight = (BigDecimal) result[2];
                    }
                    return BodyInfoResponse.WeightChange.builder()
                            .date(LocalDate.of(year, month,1))
                            .weight(averageWeight)
                            .build();
                })
                .collect(Collectors.toList());

        return Optional.of(BodyInfoResponse.MonthlyWeightChange.builder()
                .monthlyWeightList(Optional.of(weightChanges))
                .build());
    }

    public Optional<BodyInfoResponse.MonthlyMuscleChange> getMonthlyMuscleChange(Long userId) {

        existsUser(userId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestWeightRecordDate(userId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        List<Object[]> monthlyMuscleList = bodyInfoRepository.findMonthlyMuscleChange(userId, earliestDate);

        List<BodyInfoResponse.MuscleChange> muscleChanges = monthlyMuscleList.stream()
                .map(result -> {
                    Integer year = (Integer) result[0];
                    Integer month = (Integer) result[1];
                    BigDecimal averageMuscle = null;
                    if (result[2] instanceof Double) {
                        averageMuscle = BigDecimal.valueOf((Double) result[2]);
                    } else if (result[2] instanceof BigDecimal) {
                        averageMuscle = (BigDecimal) result[2];
                    }
                    return BodyInfoResponse.MuscleChange.builder()
                            .date(LocalDate.of(year, month,1))
                            .muscle(averageMuscle)
                            .build();
                })
                .collect(Collectors.toList());

        return Optional.of(BodyInfoResponse.MonthlyMuscleChange.builder()
                .monthlyMuscleList(Optional.of(muscleChanges))
                .build());
    }

    public Optional<BodyInfoResponse.MonthlyFatChange> getMonthlyFatChange(Long userId) {

        existsUser(userId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestFatRecordDate(userId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        List<Object[]> monthlyFatList = bodyInfoRepository.findMonthlyFatChange(userId, earliestDate);

        List<BodyInfoResponse.FatChange> fatChanges = monthlyFatList.stream()
                .map(result -> {
                    Integer year = (Integer) result[0];
                    Integer month = (Integer) result[1];
                    BigDecimal averageFat = null;
                    if (result[2] instanceof Double) {
                        averageFat = BigDecimal.valueOf((Double) result[2]);
                    } else if (result[2] instanceof BigDecimal) {
                        averageFat = (BigDecimal) result[2];
                    }
                    return BodyInfoResponse.FatChange.builder()
                            .date(LocalDate.of(year, month,1))
                            .fat(averageFat)
                            .build();
                })
                .collect(Collectors.toList());

        return Optional.of(BodyInfoResponse.MonthlyFatChange.builder()
                .monthlyFatList(Optional.of(fatChanges))
                .build());
    }

    public Optional<BodyInfoResponse.MonthlyBmiChange> getMonthlyBmiChange(Long userId) {

        existsUser(userId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestBmiRecordDate(userId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        List<Object[]> monthlyFatList = bodyInfoRepository.findMonthlyBmiChange(userId, earliestDate);

        List<BodyInfoResponse.BmiChange> bmiChanges = monthlyFatList.stream()
                .map(result -> {
                    Integer year = (Integer) result[0];
                    Integer month = (Integer) result[1];
                    BigDecimal averageBmi = null;
                    if (result[2] instanceof Double) {
                        averageBmi = BigDecimal.valueOf((Double) result[2]);
                    } else if (result[2] instanceof BigDecimal) {
                        averageBmi = (BigDecimal) result[2];
                    }
                    return BodyInfoResponse.BmiChange.builder()
                            .date(LocalDate.of(year, month,1))
                            .bmi(averageBmi)
                            .build();
                })
                .collect(Collectors.toList());

        return Optional.of(BodyInfoResponse.MonthlyBmiChange.builder()
                .monthlyBmiList(Optional.of(bmiChanges))
                .build());
    }

        private void existsUser(Long userId) {
            if(userRepository.findById(userId).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 유저가 없습니다.");
            }
        }

}


