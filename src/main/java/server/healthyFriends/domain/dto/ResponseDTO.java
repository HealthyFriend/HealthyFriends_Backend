package server.healthyFriends.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.healthyFriends.domain.enums.ResponseStatus;


@Getter
@AllArgsConstructor
public class ResponseDTO<T> {

    private final int code;
    private final ResponseStatus status;
    private final String message;
    private final T data;

}
