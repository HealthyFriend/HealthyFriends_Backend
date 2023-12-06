package server.healthyFriends.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ObjectiveRequest {
    @Schema(description = "목표 시작 날짜", nullable = false, example = "2023-12-15")
    private LocalDate start_day;

    @Schema(description = "목표 종료 날짜", nullable = false, example = "2024-03-01")
    private LocalDate end_day;

    @Size(min = 2,max = 20)
    @Schema(description = "목표 제목", nullable = false, example = "매일매일 운동하기")
    private String head;

    @Size(min = 2,max = 100)
    @Schema(description = "목표 내용", nullable = false, example = "매일 오전엔 달리기, 오후엔 헬스")
    private String body;
}
