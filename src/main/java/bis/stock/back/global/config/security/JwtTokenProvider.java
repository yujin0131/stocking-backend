package bis.stock.back.global.config.security;

import bis.stock.back.domain.user.UserRole;
import bis.stock.back.global.util.CookieUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String secretKey = "beautyinsidestocking";

    // 토큰 유효시간 30분
    public final Long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L;
    public final Long REFRESH_TOKEN_VALID_TIME = 60 * 60 * 1000 * 24 * 14L;

    private final UserDetailsService userDetailService;
    private final CookieUtil cookieUtil;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String userName, List<UserRole> roles) {

        Claims claims = Jwts.claims().setSubject(userName); // JWT payload에 저장되는 정보 단위
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME)) // set Expire time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret 값 세팅
                .compact();
    }

    public String createRefreshToken(String userName, List<UserRole> roles) {

        Claims claims = Jwts.claims().setSubject(userName); // JWT payload에 저장되는 정보 단위
        claims.put("role", roles); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME)) // set Expire time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret 값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        Cookie tokenCookie = cookieUtil.getCookie(request, "accessToken");
        if(tokenCookie != null) {
            return tokenCookie.getValue();
        }
        return null;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean refreshTokenRedisCheck(String refreshToken) {
        return true;
    }
}
