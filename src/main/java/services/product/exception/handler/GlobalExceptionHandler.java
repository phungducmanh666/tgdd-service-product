package services.product.exception.handler;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import services.product.data.response.ApiErrorResponse;
import services.product.exception.custome.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(400).body(
                ApiErrorResponse
                        .builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleDataAccessException(DataAccessException e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(
                ApiErrorResponse
                        .builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleDataAccessException(
            org.springframework.web.bind.MethodArgumentNotValidException e) {

        return ResponseEntity.status(400).body(
                ApiErrorResponse
                        .builder()
                        .code("BAD_REQUEST")
                        .message(e.getMessage())
                        .build());
    }

}
