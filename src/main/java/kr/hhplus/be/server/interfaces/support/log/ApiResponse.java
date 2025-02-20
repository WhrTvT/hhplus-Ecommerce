package kr.hhplus.be.server.interfaces.support.log;

import io.micrometer.common.lang.Nullable;

public record ApiResponse<T>(
        boolean success,
        String message,
        @Nullable T data
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "요청이 성공적으로 처리되었습니다.", data);
    }

    public static ApiResponse<?> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static ApiResponse<?> error(String message, Object data) {
        return new ApiResponse<>(false, message, data);
    }
}