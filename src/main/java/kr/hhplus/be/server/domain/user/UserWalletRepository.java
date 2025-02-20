package kr.hhplus.be.server.domain.user;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWalletRepository {

    Optional<UserWallet> findByUserIdWithLock(long userId);

    UserWallet save(UserWallet userWallet);
}
