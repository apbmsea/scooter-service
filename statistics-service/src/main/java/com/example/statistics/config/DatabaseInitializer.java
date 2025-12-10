/*
package com.example.statistics.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final DataSource dataSource;
    
    @Override
    public void run(String... args) throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        
        // Создание основной таблицы событий аренды
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS rental_events (
                timestamp DateTime,
                rental_id String,
                user_id String,
                scooter_id String,
                event_type Enum8('started' = 1, 'ended' = 2),
                location Tuple(Float64, Float64),
                battery_level UInt8,
                duration_seconds UInt32,
                revenue Decimal(10,2),
                city String
            ) ENGINE = MergeTree()
            PARTITION BY toYYYYMM(timestamp)
            ORDER BY (timestamp, scooter_id, city)
        """);
        
        // Создание таблицы платежных событий
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS payment_events (
                timestamp DateTime,
                payment_id String,
                rental_id String,
                user_id String,
                amount Decimal(10,2),
                currency String,
                status Enum8('succeeded' = 1, 'failed' = 2, 'pending' = 3),
                payment_method String
            ) ENGINE = MergeTree()
            PARTITION BY toYYYYMM(timestamp)
            ORDER BY (timestamp, user_id, status)
        """);
        
        // Создание таблицы пользовательских событий
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS user_events (
                timestamp DateTime,
                user_id String,
                event_type String,
                email String,
                city String,
                age UInt8
            ) ENGINE = MergeTree()
            PARTITION BY toYYYYMM(timestamp)
            ORDER BY (timestamp, user_id, event_type)
        """);
        
        // Материализованное представление для почасовой статистики
        jdbcTemplate.execute("""
            CREATE MATERIALIZED VIEW IF NOT EXISTS hourly_stats
            ENGINE = AggregatingMergeTree()
            PARTITION BY toYYYYMM(date)
            ORDER BY (date, hour, city)
            AS SELECT
                toDate(timestamp) as date,
                toHour(timestamp) as hour,
                city,
                countState() as rentals_count,
                sumState(revenue) as total_revenue,
                avgState(duration_seconds) as avg_duration
            FROM rental_events
            GROUP BY date, hour, city
        """);
        
        log.info("ClickHouse tables initialized successfully");
    }
}
*/
