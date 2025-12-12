package ru.college.PublicMessager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.college.PublicMessager.dto.PaymentEvent;
import ru.college.PublicMessager.dto.RentalEvent;
import ru.college.PublicMessager.dto.UserEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class MockGeneratorService {

    private final PublisherService publisher;
    private final AtomicInteger rentalId = new AtomicInteger(714);
    private final AtomicInteger userId = new AtomicInteger(714);
    private final AtomicInteger paymentId = new AtomicInteger(714);

    private ScheduledExecutorService executor;
    private volatile boolean running = false;

    public synchronized void start(long periodMs) {
        if (running) return;

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::generateBatch, 0, periodMs, TimeUnit.MILLISECONDS);
        running = true;
    }

    public synchronized void stop() {
        if (!running) return;
        executor.shutdownNow();
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    private void generateBatch() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        int uid = userId.getAndIncrement();
        int rid = rentalId.getAndIncrement();

        // User
        UserEvent user = new UserEvent();
        user.setUserId("user_" + uid);
        user.setEmail("user" + uid + "@example.com");
        user.setEventType("REGISTERED");
        user.setAge(rnd.nextInt(18, 60));
        user.setCity("Moscow");
        user.setTimestamp(LocalDateTime.now());
        publisher.sendUserEvent(user);

        // Rental started
        RentalEvent start = new RentalEvent();
        start.setRentalId("rental_" + rid);
        start.setUserId("user_" + uid);
        start.setScooterId("scooter_" + rnd.nextInt(1, 200));
        start.setEventType(RentalEvent.EventType.STARTED);
        start.setTimestamp(LocalDateTime.now());
        start.setBatteryLevel(rnd.nextInt(20, 90));
        start.setLocation(new RentalEvent.Location(55.75 + rnd.nextDouble(-0.01, 0.01),
                37.61 + rnd.nextDouble(-0.01, 0.01)));
        publisher.sendRentalEvent(start);

        // Rental ended
        RentalEvent end = new RentalEvent();
        end.setRentalId("rental_" + rid);
        end.setUserId("user_" + uid);
        end.setScooterId(start.getScooterId());
        end.setEventType(RentalEvent.EventType.ENDED);
        end.setTimestamp(LocalDateTime.now().plusMinutes(rnd.nextInt(1, 30)));
        end.setBatteryLevel(rnd.nextInt(10, start.getBatteryLevel()));
        end.setDurationSeconds(rnd.nextLong(60, 1200));
        end.setRevenue(rnd.nextDouble(30, 500));
        end.setLocation(start.getLocation());
        publisher.sendRentalEvent(end);

        // Payment
        PaymentEvent payment = new PaymentEvent();
        payment.setPaymentId("pay_" + paymentId.getAndIncrement());
        payment.setRentalId("rental_" + rid);
        payment.setUserId("user_" + uid);
        payment.setAmount(BigDecimal.valueOf(rnd.nextDouble(30, 500)));
        payment.setCurrency("RUB");
        payment.setPaymentMethod("SBP");
        payment.setTimestamp(LocalDateTime.now());
        payment.setStatus(PaymentEvent.PaymentStatus.SUCCEEDED);

        publisher.sendPaymentEvent(payment);
    }
}