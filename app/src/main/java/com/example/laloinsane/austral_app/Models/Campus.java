package com.example.laloinsane.austral_app.Models;

public class Campus {
    private int id_campus;
    private String nombre_campus;
    private String direccion_campus;
    private double latitud_campus;
    private double longitud_campus;

    public int getId_campus() {
        return id_campus;
    }

    public void setId_campus(int id_campus) {
        this.id_campus = id_campus;
    }

    public String getNombre_campus() {
        return nombre_campus;
    }

    public void setNombre_campus(String nombre_campus) {
        this.nombre_campus = nombre_campus;
    }

    public String getDireccion_campus() {
        return direccion_campus;
    }

    public void setDireccion_campus(String direccion_campus) {
        this.direccion_campus = direccion_campus;
    }

    public double getLatitud_campus() {
        return latitud_campus;
    }

    public void setLatitud_campus(double latitud_campus) {
        this.latitud_campus = latitud_campus;
    }

    public double getLongitud_campus() {
        return longitud_campus;
    }

    public void setLongitud_campus(double longitud_campus) {
        this.longitud_campus = longitud_campus;
    }
}
