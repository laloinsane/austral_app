package com.example.laloinsane.austral_app.Tools;

public class Haversine {
    private double Radius; // R = earth's radius (mean radius = 6,371km)

    public Haversine(double R) {
        Radius = R;
    }

    public double CalculationByDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }
}
