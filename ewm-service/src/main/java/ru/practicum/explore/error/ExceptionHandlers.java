package ru.practicum.explore.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(final NotFoundException exception) {
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .status(HttpStatus.NOT_FOUND)
                .reason("Incorrectly made request.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final BadRequest badRequest) {
        return ApiError.builder()
                .errors(List.of(badRequest.getClass().getName()))
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(badRequest.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleViolationException(DataIntegrityViolationException exception) {
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCONFLICT(final ConflictException exception) {
        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternalServerError(final HttpServerErrorException.InternalServerError error) {
        return ApiError.builder()
                .errors(List.of(error.getClass().getName()))
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(error.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidError(MethodArgumentNotValidException ex) {
        return ApiError.builder()
                .errors(List.of(ex.getClass().getName()))
                .message(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage())
                .reason(ex.getObjectName() + "\n" + ex.getParameter())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleThrowableExceptions(final Throwable error) {
        return ApiError.builder()
                .errors(List.of(error.getClass().getName()))
                .status(HttpStatus.BAD_REQUEST)
                .message(error.getMessage())
                .reason("Throwable exception")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
