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

        BigDecimal kg = req.getWeight();
        BigDecimal m = user.getHeight().divide(new BigDecimal(100),3,RoundingMode.HALF_UP);

        BigDecimal bmi = kg.divide(m.pow(2), 2, RoundingMode.HALF_UP);

        bodycompositionRecord.setBmi(bmi);

        bodyInfoRepository.save(bodycompositionRecord);

        return BodyInfoConverter.bodyInfoCreateResponse(bodycompositionRecord.getId());
    }

    public void updateBodyInfo(Long bodyCompositionRecordId, BodyInfoRequest.UpdateBodyInfoRequest req) {

        BodycompositionRecord bodycompositionRecord=bodyInfoRepository.findById(bodyCompositionRecordId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 체성분 기록이 없습니다."));

        if(req.getEdit_day()==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정하려는 날짜는 필수 입력입니다.");
        }

        if(bodyInfoRepository.existsByuserIdAndDate(bodycompositionRecord.getId(),req.getEdit_day()))
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

    public Optional<BodyInfoResponse.DailyWeightChange> getDailyWeightChange(Long userId, Long friendId) {

        if(userRepository.findById(friendId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 친구가 없습니다.");
        }

        if(!friendRepository.existsByUserIdAndFriendIdAndStatus(userId,friendId,true)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"해당 유저는 본인의 친구가 아닙니다.");
        }

        LocalDate earliestDate = bodyInfoRepository.findEarliestRecordDate(friendId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        else {
            LocalDate firstRecordDate = (earliestDate.isBefore(LocalDate.now().minusMonths(3)))
                    ? LocalDate.now().minusMonths(3)
                    : earliestDate;

            List<Object[]> dailyWeightList = bodyInfoRepository.findDailyWeightChange(friendId, firstRecordDate);

            return Optional.of(BodyInfoConverter.convertToDailyWeightChange(dailyWeightList));
        }

    }
}


