package server.healthyFriends.apiPayload;

import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.domain.enums.ResponseStatus;

public class ResponseUtil {
    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>(200, ResponseStatus.OK, message, data);
    }

    public static <T> ResponseDTO<T> created(String message, T data) {
        return new ResponseDTO<>(201, ResponseStatus.CREATED, message, data);
    }

    public static <T> ResponseDTO<T> noContent(String message, T data) {
        return new ResponseDTO<>(204, ResponseStatus.NO_CONTENT, message, data);
    }

    public static <T> ResponseDTO<T> badRequest(String message, T data) {
        return new ResponseDTO<>(400, ResponseStatus.BAD_REQUEST, message, data);
    }

    public static <T> ResponseDTO<T> methodArgumentNotValid(String message, T data) {
        return new ResponseDTO<>(400, ResponseStatus.METHOD_ARGUMENT_NOT_VALID, message, data);
    }

    public static <T> ResponseDTO<T> unauthorized(String message, T data) {
        return new ResponseDTO<>(401, ResponseStatus.UNAUTHORIZED, message, data);
    }

    public static <T> ResponseDTO<T> forbidden(String message, T data) {
        return new ResponseDTO<>(403, ResponseStatus.FORBIDDEN, message, data);
    }

    public static <T> ResponseDTO<T> notFound(String message, T data) {
        return new ResponseDTO<>(404, ResponseStatus.NOT_FOUND, message, data);
    }

    public static <T> ResponseDTO<T> conflict(String message, T data) {
        return new ResponseDTO<>(409, ResponseStatus.CONFLICT, message, data);
    }

    public static <T> ResponseDTO<T> internalServerError(String message, T data) {
        return new ResponseDTO<>(500, ResponseStatus.INTERNAL_SERVER_ERROR, message, data);
    }

    public static <T> ResponseDTO<T> serviceUnavailable(String message, T data) {
        return new ResponseDTO<>(503, ResponseStatus.SERVICE_UNAVAILABLE, message, data);
    }
}
