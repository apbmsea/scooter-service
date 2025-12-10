package com.example.statistics.repository;

public class Hotspot {
    private Double lat;
    private Double lon;
    private Long rentalCount;
    private Double avgRevenue;
    
    public Hotspot() {
    }
    
    public Hotspot(Double lat, Double lon, Long rentalCount, Double avgRevenue) {
        this.lat = lat;
        this.lon = lon;
        this.rentalCount = rentalCount;
        this.avgRevenue = avgRevenue;
    }
    
    public Double getLat() {
        return lat;
    }
    
    public void setLat(Double lat) {
        this.lat = lat;
    }
    
    public Double getLon() {
        return lon;
    }
    
    public void setLon(Double lon) {
        this.lon = lon;
    }
    
    public Long getRentalCount() {
        return rentalCount;
    }
    
    public void setRentalCount(Long rentalCount) {
        this.rentalCount = rentalCount;
    }
    
    public Double getAvgRevenue() {
        return avgRevenue;
    }
    
    public void setAvgRevenue(Double avgRevenue) {
        this.avgRevenue = avgRevenue;
    }
    
    @Override
    public String toString() {
        return "Hotspot{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", rentalCount=" + rentalCount +
                ", avgRevenue=" + avgRevenue +
                '}';
    }
}
