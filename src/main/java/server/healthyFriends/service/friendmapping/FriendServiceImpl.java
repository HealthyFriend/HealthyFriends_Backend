package server.healthyFriends.service.friendmapping;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import server.healthyFriends.converter.BodyInfoConverter;
import server.healthyFriends.converter.FriendConverter;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.repository.BodyInfoRepository;
import server.healthyFriends.repository.ObjectiveRepository;
import server.healthyFriends.service.friendmapping.FriendService;
import server.healthyFriends.web.dto.request.FriendRequest;
import server.healthyFriends.web.dto.response.BodyInfoResponse;
import server.healthyFriends.web.dto.response.FriendResponse;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.entity.mapping.FriendMapping;
import server.healthyFriends.repository.FriendRepository;
import server.healthyFriends.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final BodyInfoRepository bodyInfoRepository;

    // LoginId로 친구 찾기
    public FriendResponse.FindFriendResponse findFriendbyLoginId(String friendLoginId) {

        User friendUser = userRepository.findByLoginId(friendLoginId)
                .orElseThrow(()-> new EntityNotFoundException("해당하는 유저가 없습니다."));

        return FriendConverter.findFriendResponse(friendUser);
    }

    // Nickname으로 친구 찾기
    public FriendResponse.FindFriendResponse findFriendbyNickname(String friendNickname) {

        User friendUser = userRepository.findByNickname(friendNickname)
                .orElseThrow(()-> new EntityNotFoundException("해당하는 유저가 없습니다."));

        return FriendConverter.findFriendResponse(friendUser);
    }

    // 친구 신청
    public FriendResponse.RequestFriendResponse requestFriend(Long userId, FriendRequest.RequestFriendRequest requestFriendRequest) {

        User requestUser = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));

        // 친구 신청 대상자 정보 가져오기
        User recipientUser = userRepository.findByLoginId(requestFriendRequest.getRecipientLoginId())
                .orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));

        // 이미 친구 신청이 있는지 확인
        if (friendRepository.existsByUserIdAndFriendIdAndStatus(requestUser.getId(), recipientUser.getId(), false)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"친구 신청 중입니다.");
        }

        // 본인에게 친구 신청 불가능
        if(requestUser==recipientUser) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"본인에게 친구 신청은 불가합니다.");
        }

        // FriendMapping 엔티티 생성 및 저장 (나 -> 친구)
        FriendMapping friendMapping = new FriendMapping();
        friendMapping.setUser(requestUser);
        friendMapping.setFriendId(recipientUser.getId());
        friendMapping.setStatus(false); // 초기 상태는 false (요청중 상태)
        friendMapping = friendRepository.save(friendMapping);

        //친구 요청 성공 시 반환 DTO
        return FriendConverter.requestFrinedResponse(
                requestUser.getId(),
                recipientUser.getId(),
                friendMapping.getId()
        );
    }

    // 친구 매핑 현황
    public Optional<List<FriendResponse.MappingFriendResponse>> mappingFriend(Long userId) {
        existsUser(userId);

        List<FriendMapping> friendMappings = friendRepository.findAllByUserId(userId);

        List<FriendResponse.MappingFriendResponse> mappingFriendResponses = friendMappings.stream()
                .map(friendMapping -> {
                    User friend = userRepository.findById(friendMapping.getFriendId())
                            .orElseThrow(() -> new EntityNotFoundException("해당하는 친구가 없습니다."));
                    return FriendConverter.mappingFriendResponse(friendMapping, friend);
                })
                .toList();

        return Optional.of(mappingFriendResponses);
    }

    // 받은 친구 요청 현황 목록
    public Optional<List<FriendResponse.MappingFriendResponse>> prospectiveFriend(Long userId,Boolean isFriend) {
        existsUser(userId);

        List<FriendMapping> friendMappings = friendRepository.findAllByFriendIdAndStatus(userId,isFriend);

        List<FriendResponse.MappingFriendResponse> mappingFriendResponses = friendMappings.stream()
                .map(friendMapping -> FriendConverter.mappingFriendResponse(friendMapping, friendMapping.getUser()))
                .toList();

        return Optional.of(mappingFriendResponses);
    }

    // 친구 수락
    public FriendResponse.AcceptFriendResponse acceptFriend(Long friendMappingId) {

        FriendMapping friendMapping = friendRepository.findById(friendMappingId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 엔티티가 없습니다."));

        User requestUser = friendMapping.getUser();

        User recipientUser = userRepository.findById(friendMapping.getFriendId())
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));

        if(friendRepository.existsByUserIdAndFriendIdAndStatus(requestUser.getId(), recipientUser.getId(),true)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"이미 친구가 맺어진 상대입니다.");
        }

        // 친구 상태 true로 변환(친구 상태)
        friendMapping.setStatus(true);
        friendRepository.save(friendMapping);

        // 역순으로 테이블에 하나 더 저장
        FriendMapping friendMappingReverse = new FriendMapping();
        friendMappingReverse.setUser(recipientUser);
        friendMappingReverse.setFriendId(requestUser.getId());
        friendMappingReverse.setStatus(true);
        friendRepository.save(friendMappingReverse);

        return FriendConverter.acceptFriendResponse(recipientUser.getId(), requestUser.getId(), friendMappingReverse.getFriendId());
    }

    // 친구 요청 거절
    public void rejectFriend(Long friendMappingId) {

        FriendMapping obsoleteFriendMapping = friendRepository.findById(friendMappingId)
                        .orElseThrow(()->new EntityNotFoundException("해당하는 엔티티가 없습니다."));

        if(!obsoleteFriendMapping.getStatus()) {
            friendRepository.delete(obsoleteFriendMapping);
        }

        else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Friend Status에 문제가 있습니다.");
        }
    }

    // 친구 삭제
    public void deleteFriend(Long myId, Long friendId) {
        User myUser = userRepository.findById(myId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));
        User friend = userRepository.findById(friendId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 친구 유저가 없습니다."));

        FriendMapping friendMapping1 = friendRepository.findByUserIdAndFriendIdAndStatus(myId,friendId,true);
        FriendMapping friendMapping2 = friendRepository.findByUserIdAndFriendIdAndStatus(friendId,myId,true);

        if(friendMapping1==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 친구 관계가 없습니다.");
        }

        if(friendMapping2==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 친구 관계가 없습니다.");
        }

        friendRepository.delete(friendMapping1);
        friendRepository.delete(friendMapping2);

    }
    // 친구 목표 보기
    public FriendResponse.FriendObjective readFriendObjective(Long friendId) {

        User friendUser = userRepository.findById(friendId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 친구 유저가 존재하지 않습니다."));

        List<Objective> sortedObjectives = friendUser.getObjectiveList().stream()
                .sorted(Comparator.comparing(Objective::getStart_day).reversed())
                .toList();

        Optional<Objective> latestObjective = sortedObjectives.isEmpty()
                ? Optional.empty()
                : Optional.ofNullable(sortedObjectives.get(0));

        return FriendConverter.friendObjective(latestObjective,friendUser);
    }

    // 친구 운동 달성 기록 보기


    /**
     *  친구 체성분 변화 보기(일별)
     */
    public Optional<BodyInfoResponse.DailyWeightChange> getDailyFriendWeightChange(Long userId, Long friendId) {

        existsUser(userId);
        existsFriend(friendId);
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

        existsUser(userId);
        existsFriend(friendId);
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

        existsUser(userId);
        existsFriend(friendId);
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

        existsUser(userId);
        existsFriend(friendId);
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
     * 친구 체성분 변화 보기(월별)
     */

    public Optional<BodyInfoResponse.MonthlyWeightChange> getFriendMonthlyWeightChange(Long userId, Long friendId) {

        existsUser(userId);
        existsFriend(friendId);
        isMyFriend(userId, friendId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestWeightRecordDate(friendId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        List<Object[]> monthlyWeightList = bodyInfoRepository.findMonthlyWeightChange(friendId, earliestDate);

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

    public Optional<BodyInfoResponse.MonthlyMuscleChange> getFriendMonthlyMuscleChange(Long userId,Long friendId) {

        existsUser(userId);
        existsFriend(friendId);
        isMyFriend(userId, friendId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestWeightRecordDate(friendId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        List<Object[]> monthlyMuscleList = bodyInfoRepository.findMonthlyMuscleChange(friendId, earliestDate);

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

    public Optional<BodyInfoResponse.MonthlyFatChange> getFriendMonthlyFatChange(Long userId, Long friendId) {

        existsUser(userId);
        existsFriend(friendId);
        isMyFriend(userId, friendId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestFatRecordDate(friendId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        List<Object[]> monthlyFatList = bodyInfoRepository.findMonthlyFatChange(friendId, earliestDate);

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

    public Optional<BodyInfoResponse.MonthlyBmiChange> getFriendMonthlyBmiChange(Long userId, Long friendId) {

        existsUser(userId);
        existsFriend(friendId);
        isMyFriend(userId, friendId);

        LocalDate earliestDate = bodyInfoRepository.findEarliestBmiRecordDate(friendId);

        if(earliestDate==null) {
            return Optional.empty();
        }

        List<Object[]> monthlyFatList = bodyInfoRepository.findMonthlyBmiChange(friendId, earliestDate);

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


    private void existsFriend(Long friendId) {
        if(userRepository.findById(friendId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 친구(유저)가 없습니다.");
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