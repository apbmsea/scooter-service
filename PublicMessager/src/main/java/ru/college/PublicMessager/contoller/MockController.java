package ru.college.PublicMessager.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.college.PublicMessager.dto.PaymentEvent;
import ru.college.PublicMessager.dto.RentalEvent;
import ru.college.PublicMessager.dto.UserEvent;
import ru.college.PublicMessager.service.MockGeneratorService;
import ru.college.PublicMessager.service.PublisherService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@RestController
@RequestMapping("/mock")
@RequiredArgsConstructor
public class MockController {

    private final MockGeneratorService generator;

    @GetMapping("/start")
    public String start(@RequestParam(defaultValue = "1000") long periodMs) {
        generator.start(periodMs);
        return "Generator started. Period = " + periodMs + "ms";
    }

    @GetMapping("/stop")
    public String stop() {
        generator.stop();
        return "Generator stopped.";
    }

    @GetMapping("/status")
    public String status() {
        return generator.isRunning() ? "running" : "stopped";
    }
}
