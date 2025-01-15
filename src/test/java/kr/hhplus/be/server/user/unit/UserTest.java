package kr.hhplus.be.server.user.unit;

import kr.hhplus.be.server.domain.user.User;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class UserTest {

    @Test
    @DisplayName("userName으로 User 객체 생성")
    void createUser() {
        //given
        String userName = "장수현";

        //when
        User user = Instancio.of(User.class)
                .set(Select.field("name"), userName)
                .create();

        //then
        assertThat(user.getName()).isEqualTo(userName);
    }
}
