package com.example.statistics.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class StatisticResponse {
    private Object data;
    private String period;
    private String metric;
}

@Data
class TimeSeriesPoint {
    private String timestamp;
    private Double value;
}

@Data
class Hotspot {
    private Double lat;
    private Double lon;
    private Long rentalCount;
    private Double avgRevenue;
}
