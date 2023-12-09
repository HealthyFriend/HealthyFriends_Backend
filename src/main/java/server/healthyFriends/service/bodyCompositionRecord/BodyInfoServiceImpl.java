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

        if(req.getWeight()!=null) {

            BigDecimal kg = req.getWeight();
            BigDecimal m = user.getHeight().divide(new BigDecimal(100), 3, RoundingMode.HALF_UP);

            BigDecimal bmi = kg.divide(m.pow(2), 2, RoundingMode.HALF_UP);

            bodycompositionRecord.setBmi(bmi);
        }

        bodyInfoRepository.save(bodycompositionRecord);

        return BodyInfoConverter.bodyInfoCreateResponse(bodycompositionRecord.getId());
    }

    public void updateBodyInfo(Long bodyCompositionRecordId, BodyInfoRequest.UpdateBodyInfoRequest req) {

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
    }

    /**
     *  친구의 체성분 기록 조회
     */
    public Optional<BodyInfoResponse.DailyWeightChange> getDailyFriendWeightChange(Long userId, Long friendId) {

        existsFriend(userId);

        isMyFriend(userId, friendId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestWeightRecordDate(friendId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        else {
            LocalDate firstRecordDate = (earliestDate.isBefore(LocalDate.now().minusYears(1)))
                    ? LocalDate.now().minusYears(1)
                    : earliestDate;

            List<Object[]> dailyWeightList = bodyInfoRepository.findDailyWeightChange(friendId, firstRecordDate);

            return Optional.of(BodyInfoConverter.convertToDailyWeightChange(dailyWeightList));
        }
    }

    public Optional<BodyInfoResponse.DailyMuscleChange> getDailyFriendMuscleChange(Long userId, Long friendId) {

        existsFriend(userId);

        isMyFriend(userId, friendId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestMuscleRecordDate(friendId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        else {
            LocalDate firstRecordDate = (earliestDate.isBefore(LocalDate.now().minusYears(1)))
                    ? LocalDate.now().minusYears(1)
                    : earliestDate;

            List<Object[]> dailyMuscleList = bodyInfoRepository.findDailyMuscleChange(friendId, firstRecordDate);

            return Optional.of(BodyInfoConverter.convertToDailyMuscleChange(dailyMuscleList));
        }

    }

    public Optional<BodyInfoResponse.DailyFatChange> getDailyFriendFatChange(Long userId, Long friendId) {

        existsFriend(userId);

        isMyFriend(userId, friendId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestFatRecordDate(friendId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        else {
            LocalDate firstRecordDate = (earliestDate.isBefore(LocalDate.now().minusYears(1)))
                    ? LocalDate.now().minusYears(1)
                    : earliestDate;

            List<Object[]> dailyFatList = bodyInfoRepository.findDailyFatChange(friendId, firstRecordDate);

            return Optional.of(BodyInfoConverter.convertToDailyFatChange(dailyFatList));
        }

    }

    public Optional<BodyInfoResponse.DailyBmiChange> getDailyFriendBmiChange(Long userId, Long friendId) {

        existsFriend(userId);

        isMyFriend(userId, friendId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestBmiRecordDate(friendId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        else {
            LocalDate firstRecordDate = (earliestDate.isBefore(LocalDate.now().minusYears(1)))
                    ? LocalDate.now().minusYears(1)
                    : earliestDate;

            List<Object[]> dailyBmiList = bodyInfoRepository.findDailyBmiChange(friendId, firstRecordDate);

            return Optional.of(BodyInfoConverter.convertToDailyBmiChange(dailyBmiList));
        }

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

        existsFriend(userId);

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

        existsFriend(userId);

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

        existsFriend(userId);

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


        private void existsFriend(Long friendId) {
            if(userRepository.findById(friendId).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 친구가 없습니다.");
            }
        }

        private void existsUser(Long userId) {
            if(userRepository.findById(userId).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 유저가 없습니다.");
            }
        }

        private void isMyFriend(Long userId, Long friendId) {
            if(!friendRepository.existsByUserIdAndFriendIdAndStatus(userId,friendId,true)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"해당 유저는 본인의 친구가 아닙니다.");
            }
        }
}


