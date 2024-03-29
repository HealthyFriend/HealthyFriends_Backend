package server.healthyFriends.apiPayload.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitterReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.apiPayload.ResponseUtil;

import javax.naming.ServiceUnavailableException;
import java.rmi.UnexpectedException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDTO<Object>> handleBadRequestException(BadRequestException e) {
        return ResponseEntity
                .status(400)
                .body(ResponseUtil.badRequest(e.getMessage(),null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity
                .status(400)
                .body(ResponseUtil.methodArgumentNotValid(errors.toString(),null));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDTO<Object>> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        return ResponseEntity
                .status(400) // or any other appropriate status code
                .body(ResponseUtil.methodArgumentNotValid(errors.toString(), null));
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ResponseDTO<Object>> handleUnauthorized(HttpClientErrorException.Unauthorized e) {
        return ResponseEntity
                .status(401)
                .body(ResponseUtil.unauthorized(e.getMessage(),null));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseDTO<Object>> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity
                .status(401)
                .body(ResponseUtil.unauthorized(e.getMessage(), null));
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<ResponseDTO<Object>> handleForbiddenException(HttpClientErrorException.Forbidden e) {
        return ResponseEntity
                .status(403)
                .body(ResponseUtil.forbidden(e.getMessage(), null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDTO<Object>> handleAccessDeniedException(AccessDeniedException e) {
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

    @ExceptionHandler(UnexpectedException.class)
    public ResponseEntity<ResponseDTO<Object>> handleExpecationFailedException(UnexpectedException e) {
        return ResponseEntity
                .status(417)
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

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseDTO<Object>> handleResponseStatusException(ResponseStatusException e) {
        int statusCode = e.getStatusCode().value();

        if (statusCode == 400) {
            return ResponseEntity.status(400).body(ResponseUtil.badRequest(e.getReason(), null));
        }

        if (statusCode == 401) {
            return ResponseEntity.status(401).body(ResponseUtil.unauthorized(e.getReason(), null));
        }

        if (statusCode == 404) {
            return ResponseEntity.status(404).body(ResponseUtil.notFound(e.getReason(), null));
        }

        if (statusCode == 409) {
            return ResponseEntity.status(409).body(ResponseUtil.conflict(e.getReason(), null));
        } else {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ResponseUtil.internalServerError(e.getReason(), null));
        }
    }
}
