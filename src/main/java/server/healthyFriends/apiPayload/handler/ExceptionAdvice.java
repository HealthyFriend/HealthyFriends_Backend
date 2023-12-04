package server.healthyFriends.apiPayload.handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.apiPayload.ResponseUtil;

import javax.naming.ServiceUnavailableException;
@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDTO<Object>> handleBadRequestException(BadRequestException e) {
        return ResponseEntity
                .status(400)
                .body(ResponseUtil.badRequest(e.getMessage(),null));
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ResponseDTO<Object>> handleUnauthorized(HttpClientErrorException.Unauthorized e) {
        return ResponseEntity
                .status(401)
                .body(ResponseUtil.unauthorized(e.getMessage(),null));
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<ResponseDTO<Object>> handleForbiddenException(HttpClientErrorException.Forbidden e) {
        return ResponseEntity
                .status(403)
                .body(ResponseUtil.forbidden(e.getMessage(), null));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO<Object>> handleNotFoundException(EntityNotFoundException e) {
        return ResponseEntity
                .status(404)
                .body(ResponseUtil.notFound(e.getMessage(), null));
    }

    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ResponseEntity<ResponseDTO<Object>> handleConflictException(HttpClientErrorException.Conflict e) {
        return ResponseEntity
                .status(409)
                .body(ResponseUtil.conflict(e.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Object>> handleException(Exception e) {
        return ResponseEntity
                .status(500)
                .body(ResponseUtil.internalServerError("INTERNAL_SERVER_ERROR",null));
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ResponseDTO<Object>> handleServiceUnavailableException(ServiceUnavailableException e) {
        return ResponseEntity
                .status(503)
                .body(ResponseUtil.serviceUnavailable("SERVICE_UNAVAILABLE",null));
    }


}
