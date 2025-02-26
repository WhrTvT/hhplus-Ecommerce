package kr.hhplus.be.server.interfaces.support.exception;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
public class CustomException extends RuntimeException {
    private final ExceptionCode exceptionCode;
    private final LogLevel logLevel;
    private final Object data;

    public CustomException(ExceptionCode exceptionCode, LogLevel logLevel, Object data) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        this.logLevel = logLevel;
        this.data = data;
    }

    public CustomException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        this.logLevel = LogLevel.INFO;
        this.data = null;
    }
}