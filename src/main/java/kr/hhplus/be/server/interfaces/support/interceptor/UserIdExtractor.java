package kr.hhplus.be.server.interfaces.support.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class UserIdExtractor {

    /**
     * PathVariable 또는 RequestBody에서 userID를 추출하는 메서드
     * @param request HttpServletRequest 객체
     * @return 추출된 userID (Long) 또는 null
     */
    public static Long extractUserId(HttpServletRequest request) {
        log.debug("request method: {}", request.getMethod());

        Long userId = null;
        if("GET".equalsIgnoreCase(request.getMethod())){
            userId = extractUserIdFromPath(request);
        } else if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod()) || "PATCH".equalsIgnoreCase(request.getMethod())) {
            userId = extractUserIdFromBody(request);
        } else {
            userId = extractUserIdFromPath(request);
        }

        return userId;
    }

    /**
     * PathVariable에서 userId를 추출
     */
    private static Long extractUserIdFromPath(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        try {
            String[] uriParts = requestURI.split("/");
            String lastPart = uriParts[uriParts.length - 1]; // URI의 마지막 부분
            return Long.valueOf(lastPart);
        } catch (NumberFormatException e) {
            log.debug("No valid userId found in path: {}", requestURI);
        }

        return null;
    }

    /**
     * RequestBody에서 userId를 추출
     */
    private static Long extractUserIdFromBody(HttpServletRequest request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> body = objectMapper.readValue(request.getInputStream(), Map.class);
            if (body.containsKey("userId")) {
                return ((Number) body.get("userId")).longValue();
            }
        } catch (Exception e) {
            log.debug("Failed to parse request body for userId", e);
        }

        return null;
    }
}