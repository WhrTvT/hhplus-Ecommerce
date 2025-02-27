package kr.hhplus.be.server.interfaces.support.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.be.server.application.UserUseCase;
import kr.hhplus.be.server.application.out.UserWalletInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCheckInterceptor implements HandlerInterceptor {

    private final UserUseCase userUseCase;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        Long userId;

        log.info("유저 체크 인터셉터 실행 {}", requestURI);

        String method = request.getMethod();
        if ("POST".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) {
            // 헤더에서 userId를 추출합니다.
            String userIdHeader = request.getHeader("X-User-Id");
            if (userIdHeader == null) {
                log.info("헤더에 유저 정보가 없습니다.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return false;
            }

            userId = Long.parseLong(userIdHeader);
        } else {
            userId = UserIdExtractor.extractUserId(request);
        }

        if (userId == null) {
            log.info("유효하지 않은 사용자");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        UserWalletInfo userWalletInfo = userUseCase.userWallet(userId);
        if (userWalletInfo == null) {
            log.info("존재하지 않는 사용자: {}", userId);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}
