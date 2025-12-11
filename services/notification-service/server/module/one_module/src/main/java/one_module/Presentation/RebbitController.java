package one_module.Presentation;


import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/v1")
@OpenAPIDefinition(info = @Info(title = "My API", version = "1.0"))
public class RebbitController {
    @PostMapping("/getEvents")
    public ResponseEntity<String> getEvent(@RequestBody String json) {
        return ResponseEntity.ok(json); // Возвращаем JSON ответ
    }

    @ApiOperation(value = "Получить данные", notes = "Возвращает список данных.")
    @GetMapping("/get") // Исправлено название метода
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("getвввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввв"); // Возвращаем простой текстовый ответ
    }

    @ApiOperation(value = "Получить данные", notes = "Возвращает список данных.")
    @GetMapping("/docs")
    public List<String> showDocs() {
        return Arrays.asList("item1", "item2", "item3");
    }

    @PostMapping("/data")
    public Map<String, Object> processData(@RequestBody Map<String, Object> payload) {
        // Здесь логика обработки полученного тела запроса
        return Collections.singletonMap("message", "Данные получены успешно");
    }
}



