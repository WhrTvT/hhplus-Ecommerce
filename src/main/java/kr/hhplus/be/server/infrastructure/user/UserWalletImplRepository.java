package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.UserWallet;
import kr.hhplus.be.server.domain.user.UserWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserWalletImplRepository implements UserWalletRepository {
    private final UserWalletJpaRepository userWalletJpaRepository;

    @Override
    @Transactional
    public Optional<UserWallet> findByUserIdWithLock(long userId) {
        return userWalletJpaRepository.findByUserIdWithLock(userId);
    }

    @Override
    public UserWallet save(UserWallet userWallet) {
        return userWalletJpaRepository.save(userWallet);
    }
}
