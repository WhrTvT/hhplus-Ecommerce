/**
 * 스프링부트에서 로그 관리를 위해서는 크게는 3가지 필터,인터셉터,AOP 를 사용할 수가 있다.
 * 하지만 이 중 필터만이 유일하게 스프링 외부에서 동작하기에 Request와 Response 등을 직접 조작할 수가 있다는 이점이 있다.
 * 단, 스프링 외부에서 동작하기에 요청을 직접 읽어야하는 상황이 발생하는데 이러한 서블릿 요청은 단 한 번만 읽을 수 있도록 설계가 되어있다. 때문에 Request, Response 로그를 띄우기 위해 해당 요청을 읽어버리면 이후 해당 요청에는 값이 사라져 정상적으로 작동을 할 수 없게 된다.
 * 이러한 문제를 해결하기 위해 스프링에서는 ContentCachingRequestWrapper 라는 것을 지원하는데, 쉽게 말해 요청에 담겨있는 내용을 캐싱하여 담아두는 즉 복사하는 형태이다. 이를 이용하여 요청을 여러번 읽어 위의 문제를 해결할 수 있다.
 */

package kr.hhplus.be.server.interfaces.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter(urlPatterns = {"/*"}) // 모든 URI에 필터 등록
@Slf4j
public class LogFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI(); // 수행되는 URI

        // ContentCachingWrapper를 이용해 request와 response 래핑
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        log.info("Request 방향 Filter 수행: {}", requestURI); // Request 방향 Filter

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);

            // Request Body 로깅
            String requestBody = getContentAsString(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
            if (!requestBody.isEmpty()) {
                log.info("Request Body: {}", requestBody);
            }

            // Response Body 로깅 (바이너리 검출)
            if (isBinaryContent(responseWrapper)) {
                log.info("Response Body: [Binary Content Detected]");
            } else {
                String responseBody = getContentAsString(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());
                log.info("Response Body: {}", responseBody);
            }
        } finally {
            log.info("Response 방향 Filter 수행: {}", requestURI);
            responseWrapper.copyBodyToResponse(); // 응답 콘텐츠 클라이언트로 전달
        }
    }

    /**
     * 바이트 배열을 문자열로 변환하는 유틸리티 메서드
     */
    private String getContentAsString(byte[] content, String encoding) {
        try {
            if (content == null || content.length == 0) {
                return "";
            }
            return new String(content, encoding != null ? encoding : StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            log.error("Error while converting content to string: ", e);
            return "[Error while reading content]";
        }
    }

    /**
     * 바이너리 콘텐츠 여부를 판별하는 유틸리티 메서드
     */
    private boolean isBinaryContent(ContentCachingResponseWrapper responseWrapper) {
        String contentType = responseWrapper.getContentType();
        if (contentType == null) {
            return false; // Content-Type이 없으면 바이너리로 간주하지 않음
        }

        // 바이너리 MIME 타입 확인
        return contentType.startsWith("image/") || contentType.startsWith("application/octet-stream")
                || contentType.startsWith("application/pdf") || contentType.startsWith("video/")
                || contentType.startsWith("audio/");
    }
}

