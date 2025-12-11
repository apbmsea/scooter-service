package one_module.Presentation;

import one_module.Infrastructure.Service.v1.Redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRedisController {
    private final RedisService redisService;

    @Autowired
    public TestRedisController(RedisService redisService) {
        this.redisService = redisService;
    }

//    @GetMapping("/test")
//    public String test() {
//        redisService.setValue("key", "Hello from Redis!");
//        return (String) redisService.getValue("key");
//    }
}
