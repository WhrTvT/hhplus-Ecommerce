package kr.hhplus.be.server.interfaces.support.handler;

import jakarta.servlet.http.HttpServletRequest;
import kr.hhplus.be.server.interfaces.support.exception.CustomException;
import kr.hhplus.be.server.interfaces.support.log.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
class ApiControllerAdvice {
    // 내가 정의한 Exception 이 발생했을 때 에러 응답
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        switch (e.getLogLevel()) {
            case ERROR -> log.error("ApiException : {}", e.getMessage(), e);
            case WARN -> log.warn("ApiException : {}", e.getMessage(), e);
            default -> log.info("ApiException : {}", e.getMessage(), e);
        }
        // Http status 200 선호 "UserNotFound --> x 200"
        return ResponseEntity
                .status(e.getExceptionCode().getHttpStatus())
                .body(ApiResponse.error(e.getMessage(), e.getData()));
    }

    // 요청 본문 누락 또는 읽기 실패 시 처리 (HttpMessageNotReadableException)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
        log.error("Request body is missing or unreadable: {}", e.getMessage(), e);
        // 캐싱된 요청 본문이 있다면 로깅 (RequestBodyAdviceAdapter나 Filter에서 미리 저장한 경우)
        Object cached = request.getAttribute("cachedRequestBody");
        if (cached != null) {
            log.error("Cached request body: {}", cached);
        } else {
            // 캐싱된 데이터가 없을 경우, 추가 시도: (옵션)
            try {
                // 만약 원본 InputStream을 재사용할 수 있다면...
                String body = new String(request.getInputStream().readAllBytes());
                log.error("Request body from input stream: {}", body);
            } catch (Exception ex) {
                log.error("Failed to read request input stream", ex);
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("필수 요청 본문이 누락되었거나 읽을 수 없습니다.", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("UnhandledException : {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("50000", "서버 내부 오류가 발생했습니다."));
    }
}