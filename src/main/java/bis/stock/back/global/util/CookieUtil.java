package bis.stock.back.global.util;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class CookieUtil {

    // 쿠키 생성
    public Cookie createCookie(String cookieName, String value, Long expireTime, String path) {
        Cookie token = new Cookie(cookieName, value);
        token.setHttpOnly(true);
        token.setMaxAge(Math.toIntExact(expireTime) / 1000);
        token.setPath(path);
        return token;
    }

    // Http 응답으로 부터 쿠키 가져오기
    public Cookie getCookie(HttpServletRequest req, String cookieName) {
        final Cookie[] cookies = req.getCookies();
        if(cookies == null) return null;
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;
    }
}
