package kr.hhplus.be.server.infrastructure.user;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.user.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWalletJpaRepository extends JpaRepository<UserWallet, Long> {

    @Query("SELECT wallet FROM UserWallet wallet WHERE wallet.userId = ?1")
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<UserWallet> findByUserIdWithLock(long userId);
}
