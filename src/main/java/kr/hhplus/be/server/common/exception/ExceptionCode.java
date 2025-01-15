package kr.hhplus.be.server.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionCode {
    TEST_ERROR(HttpStatus.BAD_REQUEST, "테스트 에러입니다."), // Test Error
    NOT_FOUND_END_POINT(HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."), // 404 Not Found
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."), // 500 Internal Server Error

    NOT_FOUND_SERVICE(HttpStatus.NOT_FOUND, "Not Found"),
    CONFLICT_SERVICE(HttpStatus.CONFLICT, "Conflict"),
    PAYMENT_REQUIRED_SERVICE(HttpStatus.PAYMENT_REQUIRED, "Payment Required");

    private final HttpStatus httpStatus;	// HttpStatus
    private final String message;			// 설명
}