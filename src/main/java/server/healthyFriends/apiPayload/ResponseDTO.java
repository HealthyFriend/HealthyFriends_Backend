package server.healthyFriends.apiPayload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import server.healthyFriends.domain.enums.ResponseStatus;


@Getter
@AllArgsConstructor
public class ResponseDTO<T> {

    @Schema(description = "상태 코드", nullable = false)
    private final int code;
    @Schema(description = "HttpStatus", nullable = false)
    private final ResponseStatus status;
    @Schema(description = "상태 메시지", nullable = true)
    private final String message;
    @Schema(description = "데이터", nullable = true)
    private final T data;

}
