package com.example.statistics.repository;

import com.example.statistics.model.RentalEvent;
import com.example.statistics.model.PaymentEvent;
import com.example.statistics.model.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Repository
public class StatisticsRepository {
    
    private static final Logger log = LoggerFactory.getLogger(StatisticsRepository.class);
    
    private final JdbcTemplate jdbcTemplate;
    
    public StatisticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    // Внутренние классы для репозитория
    public static class TimeSeriesPoint {
        private String timestamp;
        private Double value;
        
        public TimeSeriesPoint() {}
        
        public TimeSeriesPoint(String timestamp, Double value) {
            this.timestamp = timestamp;
            this.value = value;
        }
        
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public Double getValue() { return value; }
        public void setValue(Double value) { this.value = value; }
    }
    
    public static class Hotspot {
        private Double lat;
        private Double lon;
        private Long rentalCount;
        private Double avgRevenue;
        
        public Hotspot() {}
        
        public Hotspot(Double lat, Double lon, Long rentalCount, Double avgRevenue) {
            this.lat = lat;
            this.lon = lon;
            this.rentalCount = rentalCount;
            this.avgRevenue = avgRevenue;
        }
        
        public Double getLat() { return lat; }
        public void setLat(Double lat) { this.lat = lat; }
        public Double getLon() { return lon; }
        public void setLon(Double lon) { this.lon = lon; }
        public Long getRentalCount() { return rentalCount; }
        public void setRentalCount(Long rentalCount) { this.rentalCount = rentalCount; }
        public Double getAvgRevenue() { return avgRevenue; }
        public void setAvgRevenue(Double avgRevenue) { this.avgRevenue = avgRevenue; }
    }
    
    // Сохранение событий аренды
    public void saveRentalEvent(RentalEvent event) {
        try {
            String sql = """
                INSERT INTO rental_events 
                (timestamp, rental_id, user_id, scooter_id, event_type, location, battery_level, duration_seconds, revenue, city)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            
            jdbcTemplate.update(sql,
                event.getTimestamp(),
                event.getRentalId(),
                event.getUserId(),
                event.getScooterId(),
                event.getEventType().name().toLowerCase(),
                event.getLocation() != null ? 
                    new Object[]{event.getLocation().getLon(), event.getLocation().getLat()} : null,
                event.getBatteryLevel(),
                event.getDurationSeconds(),
                event.getRevenue(),
                event.getCity()
            );
            log.debug("Saved rental event: {}", event.getRentalId());
        } catch (Exception e) {
            log.error("Error saving rental event {}: {}", event.getRentalId(), e.getMessage());
            throw e;
        }
    }
    
    // Сохранение платежных событий
    public void savePaymentEvent(PaymentEvent event) {
        try {
            String sql = """
                INSERT INTO payment_events 
                (timestamp, payment_id, rental_id, user_id, amount, currency, status, payment_method)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
            
            jdbcTemplate.update(sql,
                event.getTimestamp(),event.getPaymentId(),
                event.getRentalId(),
                event.getUserId(),
                event.getAmount(),
                event.getCurrency(),
                event.getStatus().name().toLowerCase(),
                event.getPaymentMethod()
            );
            log.debug("Saved payment event: {}", event.getPaymentId());
        } catch (Exception e) {
            log.error("Error saving payment event {}: {}", event.getPaymentId(), e.getMessage());
            throw e;
        }
    }
    
    // Сохранение пользовательских событий
    public void saveUserEvent(UserEvent event) {
        try {
            String sql = """
                INSERT INTO user_events 
                (timestamp, user_id, event_type, email, city, age)
                VALUES (?, ?, ?, ?, ?, ?)
            """;
            
            jdbcTemplate.update(sql,
                event.getTimestamp(),
                event.getUserId(),
                event.getEventType(),
                event.getEmail(),
                event.getCity(),
                event.getAge() != null ? event.getAge() : 0
            );
            log.debug("Saved user event: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Error saving user event {}: {}", event.getUserId(), e.getMessage());
            throw e;
        }
    }
    
    // Статистика по поездкам за период
    public List<TimeSeriesPoint> getTripsStatistics(LocalDateTime start, LocalDateTime end, String aggregation) {
        try {
            String timeFormat = "hourly".equals(aggregation) ? "toStartOfHour(timestamp)" : "toDate(timestamp)";
            
            String sql = String.format("""
                SELECT 
                    %s as time_point,
                    count(*) as trips_count
                FROM rental_events 
                WHERE timestamp BETWEEN ? AND ?
                GROUP BY time_point
                ORDER BY time_point
            """, timeFormat);
            
            return jdbcTemplate.query(sql, new RowMapper<TimeSeriesPoint>() {
                @Override
                public TimeSeriesPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new TimeSeriesPoint(
                        rs.getString("time_point"),
                        rs.getDouble("trips_count")
                    );
                }
            }, start, end);
        } catch (Exception e) {
            log.error("Error getting trips statistics: {}", e.getMessage());
            throw e;
        }
    }
    
    // Статистика по выручке за период
    public List<TimeSeriesPoint> getRevenueStatistics(LocalDateTime start, LocalDateTime end, String aggregation) {
        try {
            String timeFormat = "hourly".equals(aggregation) ? "toStartOfHour(timestamp)" : "toDate(timestamp)";
            
            String sql = String.format("""
                SELECT 
                    %s as time_point,
                    sum(revenue) as total_revenue
                FROM rental_events 
                WHERE timestamp BETWEEN ? AND ? 
                    AND event_type = 'ended'
                GROUP BY time_point
                ORDER BY time_point
            """, timeFormat);
            
            return jdbcTemplate.query(sql, new RowMapper<TimeSeriesPoint>() {
                @Override
                public TimeSeriesPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new TimeSeriesPoint(
                        rs.getString("time_point"),
                        rs.getDouble("total_revenue")
                    );
                }
            }, start, end);
        } catch (Exception e) {
            log.error("Error getting revenue statistics: {}", e.getMessage());
            throw e;
        }
    }
    
    // Популярные точки аренды
    public List<Hotspot> getHotspots(LocalDateTime start, LocalDateTime end, int limit) {
        try {
            String sql = """
                SELECT 
                    round(location.1, 3) as lon,round(location.2, 3) as lat,
                    count(*) as rental_count,
                    avg(revenue) as avg_revenue
                FROM rental_events
                WHERE timestamp BETWEEN ? AND ? 
                    AND event_type = 'started'
                GROUP BY lon, lat
                HAVING rental_count > 5
                ORDER BY rental_count DESC
                LIMIT ?
            """;
            
            return jdbcTemplate.query(sql, new RowMapper<Hotspot>() {
                @Override
                public Hotspot mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Hotspot(
                        rs.getDouble("lat"),
                        rs.getDouble("lon"),
                        rs.getLong("rental_count"),
                        rs.getDouble("avg_revenue")
                    );
                }
            }, start, end, limit);
        } catch (Exception e) {
            log.error("Error getting hotspots: {}", e.getMessage());
            throw e;
        }
    }
    
    // Операционная аналитика: среднее время аренды
    public Double getAverageRentalDuration(LocalDateTime start, LocalDateTime end) {
        try {
            String sql = """
                SELECT avg(duration_seconds) / 60 as avg_minutes
                FROM rental_events
                WHERE timestamp BETWEEN ? AND ? 
                    AND event_type = 'ended' 
                    AND duration_seconds > 0
            """;
            
            return jdbcTemplate.queryForObject(sql, Double.class, start, end);
        } catch (Exception e) {
            log.error("Error getting average rental duration: {}", e.getMessage());
            return 0.0;
        }
    }
    
    // Коэффициент использования самокатов
    public Double getScooterUtilization(String scooterId, LocalDateTime start, LocalDateTime end) {
        try {
            String sql = """
                SELECT 
                    sum(duration_seconds) / (24 * 3600 * ?) as utilization_rate
                FROM rental_events
                WHERE scooter_id = ? 
                    AND timestamp BETWEEN ? AND ? 
                    AND event_type = 'ended'
            """;
            
            long daysBetween = java.time.Duration.between(start, end).toDays();
            daysBetween = daysBetween > 0 ? daysBetween : 1;
            
            return jdbcTemplate.queryForObject(sql, Double.class, daysBetween, scooterId, start, end);
        } catch (Exception e) {
            log.error("Error getting scooter utilization for {}: {}", scooterId, e.getMessage());
            return 0.0;
        }
    }
    
    // Retention rate пользователей
    public Double getRetentionRate(LocalDateTime periodStart, LocalDateTime periodEnd) {
        try {
            String sql = """
                WITH user_first_rental AS (
                    SELECT user_id, min(timestamp) as first_rental
                    FROM rental_events
                    WHERE event_type = 'started'
                    GROUP BY user_id
                ),
                returning_users AS (
                    SELECT DISTINCT user_id
                    FROM rental_events re
                    JOIN user_first_rental ufr ON re.user_id = ufr.user_id
                    WHERE re.timestamp BETWEEN ufr.first_rental + INTERVAL 1 DAY AND ufr.first_rental + INTERVAL 30 DAY
                    AND re.event_type = 'started'
                )
                SELECT 
                    count(DISTINCT returning_users.user_id) * 100.0 / count(DISTINCT user_first_rental.user_id) as retention_rate
                FROM user_first_rental
                LEFT JOIN returning_users ON user_first_rental.user_id = returning_users.user_id
                WHERE user_first_rental.first_rental BETWEEN ? AND ?
            """;
            
            Double result = jdbcTemplate.queryForObject(sql, Double.class, periodStart, periodEnd);
            return result != null ? result : 0.0;
        } catch (Exception e) {log.error("Error getting retention rate: {}", e.getMessage());
        return 0.0;
    }
}

// Количество поездок по городам
public List<TimeSeriesPoint> getTripsByCity(LocalDateTime start, LocalDateTime end, String city) {
    try {
        String sql = """
            SELECT 
                toDate(timestamp) as date,
                count(*) as trips_count
            FROM rental_events 
            WHERE timestamp BETWEEN ? AND ? AND city = ?
            GROUP BY date
            ORDER BY date
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<TimeSeriesPoint>() {
            @Override
            public TimeSeriesPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new TimeSeriesPoint(
                    rs.getString("date"),
                    rs.getDouble("trips_count")
                );
            }
        }, start, end, city);
    } catch (Exception e) {
        log.error("Error getting trips by city: {}", e.getMessage());
        throw e;
    }
}

// Статистика по пользователям
public Long getActiveUsersCount(LocalDateTime start, LocalDateTime end) {
    try {
        String sql = """
            SELECT count(DISTINCT user_id) as active_users
            FROM rental_events
            WHERE timestamp BETWEEN ? AND ?
        """;
        
        return jdbcTemplate.queryForObject(sql, Long.class, start, end);
    } catch (Exception e) {
        log.error("Error getting active users count: {}", e.getMessage());
        return 0L;
    }
}

// Общая выручка за период
public Double getTotalRevenue(LocalDateTime start, LocalDateTime end) {
    try {
        String sql = """
            SELECT sum(revenue) as total_revenue
            FROM rental_events
            WHERE timestamp BETWEEN ? AND ? AND event_type = 'ended'
        """;
        
        Double result = jdbcTemplate.queryForObject(sql, Double.class, start, end);
        return result != null ? result : 0.0;
    } catch (Exception e) {
        log.error("Error getting total revenue: {}", e.getMessage());
        return 0.0;
    }
}

// Простая статистика в виде Map (альтернатива)
public Map<String, Object> getBasicStats(LocalDateTime start, LocalDateTime end) {
    Map<String, Object> stats = new HashMap<>();
    try {
        String sql = """
            SELECT 
                count(*) as total_trips,
                sum(revenue) as total_revenue,
                count(DISTINCT user_id) as unique_users,
                avg(duration_seconds) as avg_duration
            FROM rental_events 
            WHERE timestamp BETWEEN ? AND ?
        """;
        
        return jdbcTemplate.queryForObject(sql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> result = new HashMap<>();
                result.put("total_trips", rs.getLong("total_trips"));
                result.put("total_revenue", rs.getDouble("total_revenue"));
                result.put("unique_users", rs.getLong("unique_users"));
                result.put("avg_duration", rs.getDouble("avg_duration"));
                return result;
            }
        }, start, end);
    } catch (Exception e) {
        log.error("Error getting basic stats: {}", e.getMessage());
        return stats;
    }
}
}