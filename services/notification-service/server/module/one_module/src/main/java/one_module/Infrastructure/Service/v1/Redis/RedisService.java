package one_module.Infrastructure.Service.v1.Redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValue(UUID key, Object value) {
        redisTemplate.opsForValue().set(String.valueOf(key), value);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Object getValueUUID(UUID id) {
        return redisTemplate.opsForValue().get(id);
    }


    public void removeValue(String key) {
        redisTemplate.delete(key);
    }

    public void clear() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

//    public  keys(String pattern) {
//        redisTemplate.expire("myKey", Duration.ofSeconds(60));
//    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public long incr(String key) {
        return redisTemplate.opsForValue().increment(key, 1);
    }

    public long decr(String key) {
        return redisTemplate.opsForValue().increment(key, -1);
    }

    public void setTTlSeconds60(String key) {
        redisTemplate.expire(key, Duration.ofSeconds(60));
    }

    public void setTTloneDay(UUID key) {
        redisTemplate.expire(String.valueOf(key), Duration.ofDays(1));
    }
}
