package kr.hhplus.be.server.interfaces.support.config;

// http://localhost:8080/swagger-ui/index.html 로 접속하여 API 명세를 받아볼 수 있다.

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .components(new Components())
                .info(customInfo())
                .servers(customServer());
    }

    // Info Setting
    private Info customInfo() {
        return new Info()
                .title("Springdoc 테스트")
                .description("Springdoc을 사용한 Swagger UI 테스트")
                .version("1.0.0")
                .description("""
                        - 상품 목록 조회 API
                        - 잔액 충전 API
                        - 잔액 조회 API
                        - 상품 주문 API
                        - 주문 결제 API
                        - 쿠폰 발급 API
                        - 쿠폰 조회 API
                        - 상위 상품 목록 조회 API
                        """);
    }

    private List<Server> customServer() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url("http://localhost:8080"));
        return servers;
    }


}