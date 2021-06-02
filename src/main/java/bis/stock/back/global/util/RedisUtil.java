package bis.stock.back.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisUtil {

    // 문자열로 redis에 serialize, deserialize를 사용하는 템플릿
    private final StringRedisTemplate stringRedisTemplate;

    // key에 담긴 값 가져오기
    public String getData(String key) {

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    // key : value 값 저장하기
    public void setData(String key, String value) {

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    // key : value 에 만료시간까지 지정해서 저장하기
    public void setDataExpire(String key, String value, long duration) {

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    // 값 삭제하기 (만료시간 설정한 값은 굳이 삭제할 필요는 없긴 하지만)
    public void deleteData(String key) {

        stringRedisTemplate.delete(key);
    }
}
