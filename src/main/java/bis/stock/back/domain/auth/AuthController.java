package bis.stock.back.domain.auth;

import bis.stock.back.domain.auth.dto.AccessTokenDto;
import bis.stock.back.domain.auth.dto.JoinDto;
import bis.stock.back.domain.auth.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<Long> join(@RequestBody JoinDto joinDto) {
    	
        return ResponseEntity.ok(authService.join(joinDto));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<AccessTokenDto> login(@RequestBody LoginDto loginDto,
                                                HttpServletResponse res) {

        return ResponseEntity.ok(authService.login(loginDto, res));
    }

    // 재발급 (refresh 토큰을 이용해 access 토큰 재발급)
    @PostMapping("reissue")
    public ResponseEntity<AccessTokenDto> reissue(HttpServletRequest req, HttpServletResponse res) {

        return ResponseEntity.ok(authService.reissue(req, res));
    }
}
