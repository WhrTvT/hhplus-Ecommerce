package kr.hhplus.be.server.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.interfaces.request.UserWalletChargeRequest;
import kr.hhplus.be.server.interfaces.response.UserWalletChargeResponse;
import kr.hhplus.be.server.interfaces.response.UserWalletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Wallet API", description = "유저의 지갑(잔액)을 관리합니다.")
@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
public class UserWalletController {

//    private static final Logger log = LoggerFactory.getLogger(UserWalletController.class);
//    private final UserService userService;

    // 잔액 충전
    @Operation(summary = "잔액 충전", description = "Body로 받은 잔액 정보로 잔액을 충전합니다.")
    @Parameter(name = "userWalletChargeRequest", description = "사용자 잔액충전 Req 정보")
    @PatchMapping("/charge")
    public ResponseEntity<UserWalletChargeResponse> chargeAmount(
            @RequestBody UserWalletChargeRequest userWalletChargeRequest
    ) {
//        UserWallet userWallet = userService.setUserWalletCharge(userWalletRequest.to(userId));
//        log.info("User charged : {}", userWallet);

//        return ResponseEntity.ok(UserWalletResponse.from(userWallet));
        return ResponseEntity.ok(UserWalletChargeResponse.mock(userWalletChargeRequest.userId()));

    }

    // 잔액 조회
    @Operation(summary = "잔액 조회", description = "Path로 받은 유저ID 정보로 잔액을 조회 합니다.")
    @Parameter(name = "userId", description = "유저ID")
    @GetMapping("/wallet/{userId}")
    public ResponseEntity<UserWalletResponse> UserWallet(
            @PathVariable Long userId
    ) {
//        UserWallet userWallet = userService.getUserWallet(userId);
//        log.info("User wallet : {}", userWallet);

//        return ResponseEntity.ok(UserWalletResponse.from(userWallet));
        return ResponseEntity.ok(UserWalletResponse.mock(userId));
    }
}
