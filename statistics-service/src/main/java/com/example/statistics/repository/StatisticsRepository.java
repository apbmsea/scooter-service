package com.example.statistics.repository;

import com.example.statistics.model.RentalEvent;
import com.example.statistics.model.PaymentEvent;
import com.example.statistics.model.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

@Repository
public class StatisticsRepository {
    
    private static final Logger log = LoggerFactory.getLogger(StatisticsRepository.class);
    
    private final JdbcTemplate jdbcTemplate;
    
    public StatisticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
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


    public void saveRentalEvent(RentalEvent event) {
        try {
            String sql = "INSERT INTO rental_events " +
                    "(timestamp, rental_id, user_id, scooter_id, event_type, " +
                    "battery_level, duration_seconds, revenue, city, lat, lon) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
            jdbcTemplate.update(sql,
                event.getTimestamp() != null
                    ? java.sql.Timestamp.valueOf(event.getTimestamp().withNano(0))
                    : java.sql.Timestamp.valueOf(LocalDateTime.now().withNano(0)),
                event.getRentalId(),
                event.getUserId(),
                event.getScooterId(),
                event.getEventType() != null ? 
                    event.getEventType().name().toLowerCase() : "started", // Enum значение
                event.getBatteryLevel() != null ? event.getBatteryLevel() : 0,
                event.getDurationSeconds() != null ? event.getDurationSeconds() : 0L,
                event.getRevenue() != null ? event.getRevenue() : BigDecimal.ZERO,
                event.getCity() != null ? event.getCity() : "",
                event.getLocation() != null ? event.getLocation().getLat() : 0.0,
                event.getLocation() != null ? event.getLocation().getLon() : 0.0
            );
            
            log.info("✅ Saved rental event: {}", event.getRentalId());
            
        } catch (Exception e) {
            log.error("❌ Error saving rental event {}: {}", event.getRentalId(), e.getMessage(), e);
            throw new RuntimeException("Error saving rental event", e);
        }
    }

    public void savePaymentEvent(PaymentEvent event) {
        try {
            String sql = "INSERT INTO payment_events " +
                    "(timestamp, payment_id, rental_id, user_id, amount, currency, status, payment_method) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
            String statusValue;
            if (event.getStatus() == null) {
                statusValue = "pending";
            } else {
                switch (event.getStatus()) {
                    case SUCCEEDED:
                        statusValue = "succeeded"; 
                        break;
                    case FAILED:
                        statusValue = "failed";    
                        break;
                    case PENDING:
                        statusValue = "pending";   
                        break;
                    default:
                        statusValue = "pending";
                }
            }
            
            jdbcTemplate.update(sql,
                event.getTimestamp() != null
                    ? java.sql.Timestamp.valueOf(event.getTimestamp().withNano(0))
                    : java.sql.Timestamp.valueOf(LocalDateTime.now().withNano(0)),
                event.getPaymentId(),
                event.getRentalId(),
                event.getUserId(),
                event.getAmount() != null ? event.getAmount() : BigDecimal.ZERO,
                event.getCurrency() != null ? event.getCurrency() : "USD",
                statusValue, 
                event.getPaymentMethod() != null ? event.getPaymentMethod() : ""
            );
            
            log.info("✅ Saved payment event: {}", event.getPaymentId());
            
        } catch (Exception e) {
            log.error("❌ Error saving payment event {}: {}", event.getPaymentId(), e.getMessage(), e);
            throw new RuntimeException("Error saving payment event", e);
        }
    }

    public void saveUserEvent(UserEvent event) {
        try {
            String sql = "INSERT INTO user_events " +
                    "(timestamp, user_id, event_type, email, city, age) VALUES (?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(sql,
                    event.getTimestamp() != null
                            ? java.sql.Timestamp.valueOf(event.getTimestamp().withNano(0))
                            : java.sql.Timestamp.valueOf(LocalDateTime.now().withNano(0)),
                    event.getUserId(),
                    event.getEventType() != null ? event.getEventType() : "",
                    event.getEmail() != null ? event.getEmail() : "",
                    event.getCity() != null ? event.getCity() : "",
                    event.getAge() != null ? event.getAge() : 0
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving user event " + event.getUserId(), e);
        }
    }

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
    
    public List<TimeSeriesPoint> getRevenueStatistics(LocalDateTime start, LocalDateTime end, String aggregation) {
        try {
            String timeFormat = "hourly".equals(aggregation) ? "toStartOfHour(timestamp)" : "toDate(timestamp)";
            
            String sql = String.format("""
                SELECT 
                    %s as time_point,
                    sum(revenue) as total_revenue
                FROM rental_events 
                WHERE timestamp BETWEEN ? AND ? 
                    AND event_type = 'ended'   -- Важно: 'ended' (строчные)
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
            log.error("Error getting revenue statistics: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    public List<Hotspot> getHotspots(LocalDateTime start, LocalDateTime end, int limit) {
        try {
            String sql = """
                SELECT 
                    round(lon, 3) as lon,        -- Изменили с location.1 на lon
                    round(lat, 3) as lat,        -- Изменили с location.2 на lat
                    count(*) as rental_count,
                    avg(revenue) as avg_revenue
                FROM rental_events
                WHERE timestamp BETWEEN ? AND ? 
                    AND event_type = 'started'   -- Важно: 'started' (строчные)
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
            log.error("Error getting hotspots: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    public Double getAverageRentalDuration(LocalDateTime start, LocalDateTime end) {
        try {
            String sql = """
                SELECT avg(duration_seconds) / 60 as avg_minutes
                FROM rental_events
                WHERE timestamp BETWEEN ? AND ? 
                    AND event_type = 'ended'   -- Важно: 'ended' (строчные)
                    AND duration_seconds > 0
            """;
            
            return jdbcTemplate.queryForObject(sql, Double.class, start, end);
        } catch (Exception e) {
            log.error("Error getting average rental duration: {}", e.getMessage(), e);
            return 0.0;
        }
    }
    
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