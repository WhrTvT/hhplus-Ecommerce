package kr.hhplus.be.server.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.application.UserUseCase;
import kr.hhplus.be.server.application.out.UserWalletChargeInfo;
import kr.hhplus.be.server.application.out.UserWalletInfo;
import kr.hhplus.be.server.domain.user.UserWallet;
import kr.hhplus.be.server.interfaces.request.UserWalletChargeRequest;
import kr.hhplus.be.server.interfaces.response.UserWalletChargeResponse;
import kr.hhplus.be.server.interfaces.response.UserWalletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Wallet API", description = "유저의 지갑(잔액)을 관리합니다.")
@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserUseCase userUseCase;

    // 잔액 충전
    @Operation(summary = "잔액 충전", description = "Body로 받은 잔액 정보로 잔액을 충전합니다.")
    @Parameter(name = "userWalletChargeRequest", description = "사용자 잔액충전 Req 정보")
    @PatchMapping("/charge")
    public ResponseEntity<UserWalletChargeResponse> userWalletCharge(
            @Valid @RequestBody UserWalletChargeRequest userWalletChargeRequest
    ) {
        UserWalletChargeInfo userWalletChargeInfo = userUseCase.userWalletCharge(userWalletChargeRequest.userId(), userWalletChargeRequest.chargeAmount());
        log.info("User charged : {}", userWalletChargeInfo);

//        return ResponseEntity.ok(UserWalletChargeResponseMock.mock(userWalletChargeRequest.walletId()));
        return ResponseEntity.ok(UserWalletChargeResponse.from(userWalletChargeInfo));
    }

    // 잔액 조회
    @Operation(summary = "잔액 조회", description = "Path로 받은 유저ID 정보로 잔액을 조회 합니다.")
    @Parameter(name = "walletId", description = "유저ID")
    @GetMapping("/wallet/{userId}")
    public ResponseEntity<UserWalletResponse> userWallet(
            @PathVariable Long userId
    ) {
        UserWalletInfo userWalletInfo = userUseCase.userWallet(userId);
        log.info("User wallet : {}", userWalletInfo);

//        return ResponseEntity.ok(UserWalletResponseMock.mock(userId));
        return ResponseEntity.ok(UserWalletResponse.from(userWalletInfo));
    }
}
