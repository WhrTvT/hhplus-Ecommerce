package kr.hhplus.be.server.interfaces;

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

@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
public class UserWalletController {

//    private static final Logger log = LoggerFactory.getLogger(UserWalletController.class);
//    private final UserService userService;

    // 잔액 충전
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
