package bis.stock.back.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    // GenericFilter를 상속 받아서 Jwt를 이용한 인증 필터 생성

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        // 헤더에서 JWT를 받아옴.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        // 유효한 토큰인지 확인함.
        if(token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로 부터 유저 정보를 받아옴
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
