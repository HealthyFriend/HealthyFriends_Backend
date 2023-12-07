package server.healthyFriends.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import server.healthyFriends.converter.BodyInfoConverter;
import server.healthyFriends.domain.entity.BodycompositionRecord;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.repository.BodyInfoRepository;
import server.healthyFriends.repository.UserRepository;
import server.healthyFriends.web.dto.request.BodyInfoRequest;
import server.healthyFriends.web.dto.response.BodyInfoResponse;
import server.healthyFriends.web.dto.response.UserResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class BodyInfoServiceImpl implements BodyInfoService{

    private final UserRepository userRepository;
    private final BodyInfoRepository bodyInfoRepository;

    public BodyInfoResponse.CreateBodyInfoResponse createBodyInfoResponse(Long userId, BodyInfoRequest.CreateBodyInfoRequest req) {

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
}
