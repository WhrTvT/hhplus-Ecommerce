package kr.hhplus.be.server.interfaces.config.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LogInterceptor logInterceptor;
    private final UserCheckInterceptor userCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(userCheckInterceptor)
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/css/**", "/*.ico", "/error",
                        "/api/commerce/products", "/api/commerce/top-product", "/api/commerce/payment", "/mock/**");
    }
}