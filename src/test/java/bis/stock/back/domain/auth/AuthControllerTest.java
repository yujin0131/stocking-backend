package bis.stock.back.domain.auth;

import bis.stock.back.domain.auth.dto.JoinDto;
import bis.stock.back.domain.auth.dto.LoginDto;
import bis.stock.back.domain.user.User;
import bis.stock.back.domain.user.UserRepository;
import bis.stock.back.domain.user.UserRole;
import bis.stock.back.global.config.security.JwtTokenProvider;
import bis.stock.back.global.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import java.util.Collections;

// ***********************************************************************************
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// ***********************************************************************************

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 실제 db에 저장되지 않고, 테스트 끝나면 롤백시킴
class AuthControllerTest {

    /**
     * Controller 테스트는 비즈니스 로직이 없음
     * 따라서 MockMvc를 이용하여 http 요청만 주고, 올바른 결과값에 대해 Assertion 하면 됨.
     */
    @Autowired
    public AuthController authController;

    protected MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void createController() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        userRepository.save(User.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("1234"))
                .nickname("test")
                .cash(0L)
                .roles(Collections.singletonList(UserRole.ROLE_USER))
                .build());
    }

    @Test
    void 회원가입() throws Exception {

        // 회원 json 값 만들기 (Given)
        String content = objectMapper.writeValueAsString(
                JoinDto.builder()
                        .email("test1@test.com")
                        .nickname("test")
                        .password("1234")
                        .build()
        );

        // mocnMvc에 쓸 RequestBuilder 설정 (When)
        // 정상 회원가입 값으로 확인
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/join")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        // 결과값 확인 (Then)
        // 참고로 status()나 print()함수는 위에 별표시한 static import를 꼭 추가하여야 함.
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andDo(print());

        // 이미 존재하는 값으로도 확인
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    void 로그인() throws Exception {

        // 로그인 json 값 만들기
        String content = objectMapper.writeValueAsString(
                LoginDto.builder()
                        .email("test@test.com")
                        .password("1234")
                        .build()
        );

        // 위와 동일
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/login")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        // http 코드와 함께 쿠키가 담겨있는지 확인.
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().exists("refreshToken"))
                .andDo(print());
    }

    @Test
    void 재발급() throws Exception {

        // beforeEach에서 미리 생성해 놓은 유저 찾기
        User user = userRepository.findByEmail("test@test.com").orElse(null);

        /**
         * 새로운 refreshToken 만들기
         * 그 후 redis에 해당 refreshToken을 저장하고 쿠키로도 만든다.
         * 왜냐, 비즈니스 로직에 redis에 저장된 refreshToken을 확인하는 절차가 있기 때문에.
         */
        String refreshTokenJwt = jwtTokenProvider.createRefreshToken(user.getUsername(), user.getRoles());
        redisUtil.setDataExpire(refreshTokenJwt, "test@test.com", 100);
        Cookie refreshToken = new Cookie("refreshToken", refreshTokenJwt);

        // 이하 똑같음
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/reissue")
                .cookie(refreshToken);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(cookie().exists("accessToken"))
                .andDo(print());
    }
}