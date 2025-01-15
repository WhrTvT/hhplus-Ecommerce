package kr.hhplus.be.server.user.unit;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.UserValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static  org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserValidator userValidator;

    @Test
    @DisplayName("🔴 유저가 존재하지 않으면 BusinessException 발생")
    void testUserNotFound() {
        // given
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> {
            userValidator.validateOfUserFindById(1L);
        }).isInstanceOf(CustomException.class).hasMessage("User Not Found");
    }

    @Test
    @DisplayName("🟢 유저가 존재하면 User 정보를 리턴")
    void testUpdateUserEmail() {
        // given
        User user = User.builder().userId(1L).build();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        User loadUser = userValidator.validateOfUserFindById(1L);

        // then
        Assertions.assertThat(user).isEqualTo(loadUser);
    }
}