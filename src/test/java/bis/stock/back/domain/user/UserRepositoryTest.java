package bis.stock.back.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // data jpa 테스트용. transactional 포함
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 테스트용 메모리 db가 아닌 실db로 테스트
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    User user = User.builder()
            .email("test@test.com")
            .password("1234")
            .cash(0L)
            .roles(Collections.singletonList(UserRole.ROLE_USER))
            .nickname("test")
            .build();

    User saveUser;

    @BeforeEach
    void 테스트유저생성() {
        saveUser = userRepository.save(user);
    }

    @Test
    void 유저저장() {
        assertThat(saveUser).isEqualTo(user);
    }

    @Test
    void 유저삭제() {
        userRepository.delete(user);
        assertThat(userRepository.findByEmail("test@test.com").orElse(null)).isNull();
    }

    @Test
    void email로유저찾기() {
        assertThat(userRepository.findByEmail("test@test.com").orElse(null)).isEqualTo(user);
    }
}