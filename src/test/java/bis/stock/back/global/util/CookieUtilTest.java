package bis.stock.back.global.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class CookieUtilTest {

    @Autowired
    private CookieUtil cookieUtil;

    private MockHttpServletRequest servletRequest;

    Cookie createTestCookie() {
        return cookieUtil.createCookie("testCookie",
                "abc",
                10L,
                "/test/cookie");
    }

    @Test
    void 쿠키생성() {
        Cookie cookie = createTestCookie();

        assertThat("testCookie").isEqualTo(cookie.getName());
        assertThat("abc").isEqualTo(cookie.getValue());
        assertThat(10/1000).isEqualTo(cookie.getMaxAge());
        assertThat("/test/cookie").isEqualTo(cookie.getPath());
    }

    @Test
    void 쿠키가져오기() {
        Cookie cookie = createTestCookie();
        servletRequest = new MockHttpServletRequest();
        servletRequest.setCookies(cookie);

        assertThat(servletRequest.getCookies()[0].getValue()).isEqualTo(cookie.getValue());
    }
}