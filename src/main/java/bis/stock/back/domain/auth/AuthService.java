package bis.stock.back.domain.auth;

import bis.stock.back.domain.auth.dto.AccessTokenDto;
import bis.stock.back.domain.auth.dto.JoinDto;
import bis.stock.back.domain.auth.dto.LoginDto;
import bis.stock.back.domain.user.User;
import bis.stock.back.domain.user.UserRepository;
import bis.stock.back.domain.user.UserRole;
import bis.stock.back.global.config.security.JwtTokenProvider;
import bis.stock.back.global.exception.ForbiddenException;
import bis.stock.back.global.exception.NotFoundException;
import bis.stock.back.global.util.CookieUtil;
import bis.stock.back.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;
    private final UserDetailsService userDetailsService;
    private final RedisUtil redisUtil;

    public Long join(JoinDto joinDto) {

        // 가입된 사용자인지 체크
        if(userRepository.findByEmail(joinDto.getEmail()).orElse(null) != null) {
            throw new NotFoundException("이미 존재하는 email입니다.");
        }

        return userRepository.save(User.builder()
                .email(joinDto.getEmail())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .nickname(joinDto.getNickname())
                .cash(0L)
                .roles(Collections.singletonList(UserRole.ROLE_USER))
                .build()).getId();
    }

    public AccessTokenDto login(LoginDto loginDto, HttpServletResponse res) {

        // 로그인한 정보로 user 엔티티 가져오기
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new InternalAuthenticationServiceException("가입되지 않은 email입니다."));

        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }

        // user 엔티티의 정보로 jwt 토큰(문자열) 만들기
        String accessTokenJwt = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRoles());
        String refreshTokenJwt = jwtTokenProvider.createRefreshToken(user.getUsername(), user.getRoles());

        // jwt 토큰을 쿠키로 만들기
        Cookie accessToken = cookieUtil.createCookie("accessToken",
                accessTokenJwt, jwtTokenProvider.ACCESS_TOKEN_VALID_TIME, "/");
        Cookie refreshToken = cookieUtil.createCookie("refreshToken",
                refreshTokenJwt, jwtTokenProvider.REFRESH_TOKEN_VALID_TIME, "/auth/reissue");

        // servlet 응답에 쿠키 추가
        res.addCookie(accessToken);
        res.addCookie(refreshToken);

        /**
         * 로그인할 때 redis에 refresh 토큰을 저장해 놓는다.
         * 이 때 refresh 토큰의 만료시간을 함께 설정해서 만료시간이 끝나면 자동으로 사라지게 함.
         * 이후 access 토큰 재발급때 확인하고 재발급함.
         */
        redisUtil.setDataExpire(refreshTokenJwt, user.getEmail(), jwtTokenProvider.REFRESH_TOKEN_VALID_TIME / 1000);

        return AccessTokenDto.builder().accessToken(accessTokenJwt).build();
    }

    public AccessTokenDto reissue(HttpServletRequest req, HttpServletResponse res) {

        // Http 요청에서부터 "refreshToken"이름의 쿠키를 받아옴.
        Cookie refreshToken;
        refreshToken = cookieUtil.getCookie(req, "refreshToken");

        // 쿠키가 없으면 만료되었다는 의미이므로 Exception
        if(refreshToken == null) {
            throw new ForbiddenException("Refresh Token Expired");
        }

        // 쿠키에서 jwt 토큰만 가져옴
        String refreshTokenJwt = refreshToken.getValue();

        // valid check
        if(jwtTokenProvider.validateToken(refreshTokenJwt)) {
            // 토큰으로부터 유저정보를 가져옴.
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenProvider.getUserPk(refreshTokenJwt));
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Refresh Token 오류"));
            /**
             * redis에 저장되어있는지 체크하는 부분.
             */
            String check = redisUtil.getData(refreshTokenJwt);
            if(check == null) {
                throw new ForbiddenException("Refresh Token이 유효하지 않습니다.");
            }
            if(check.equals(user.getEmail())) {

                // jwtTokenProvider로부터 accessToken 생성
                String accessTokenJwt = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRoles());
                // 새로운 access 토큰 쿠키를 생성
                Cookie newAccessToken = cookieUtil.createCookie("accessToken",
                        accessTokenJwt, jwtTokenProvider.ACCESS_TOKEN_VALID_TIME, "/");
                // http 응답에 실어 보낸다.
                res.addCookie(newAccessToken);

                // 마지막으로 accessToken을 http body에도 보낸다.
                return AccessTokenDto.builder().accessToken(accessTokenJwt).build();
            } else {
                // 만약 사용자 정보와 redis에 저장된 값이 같지 않다면 redis 값을 삭제하고 예외처리
                redisUtil.deleteData(refreshTokenJwt);
                throw new ForbiddenException("Refresh Token이 유효하지 않습니다.");
            }
        } else {
            // refreshToken이 유효하지 않을 때.
            throw new ForbiddenException("Refresh Token이 유효하지 않습니다.");
        }
    }
}
