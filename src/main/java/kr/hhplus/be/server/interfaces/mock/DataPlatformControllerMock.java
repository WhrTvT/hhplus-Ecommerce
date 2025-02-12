package kr.hhplus.be.server.interfaces.mock;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "DataPaltform Mock API", description = "데이터 플랫폼 Mock API 입니다.")
@RestController
@RequestMapping("/mock")
@RequiredArgsConstructor
@Slf4j
public class DataPlatformControllerMock {

    @Operation(summary = "데이터플랫폼 Mock", description = "결제 이후, 데이터플랫폼으로 데이터를 전송합니다.")
    @Parameter(name = "couponIssuedRequest", description = "쿠폰 발급 Req 정보")
    @GetMapping("/dataPlatform")
    public ApiResponse<Boolean> dataPlatform(

    ) {
        return ApiResponse.success(true);
    }
}
