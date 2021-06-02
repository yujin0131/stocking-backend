package bis.stock.back.global.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisUtilTest {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @AfterEach
    void flush() {
        redisUtil.deleteData("test");
    }

    @Test
    void redis_데이터삭제_조회() {
        redisUtil.setData("test", "abc");
        assertThat(redisUtil.getData("test")).isEqualTo("abc");
    }

    @Test
    void redis_데이터저장_유효시간() {
        redisUtil.setDataExpire("test", "abc", 10L);
        assertThat(redisTemplate.getExpire("test")).isNotNull();
        assertThat(redisUtil.getData("test")).isEqualTo("abc");
    }

    @Test
    void redis_데이터삭제() {
        redisUtil.setData("test", "abc");
        redisUtil.deleteData("test");
        assertThat(redisUtil.getData("test")).isNull();
    }
}