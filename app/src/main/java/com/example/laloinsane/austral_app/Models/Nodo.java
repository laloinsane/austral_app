package com.example.laloinsane.austral_app.Models;

import java.util.ArrayList;

public class Nodo {
    private int id_nodo;
    private int id_campus;
    private double latitud_nodo;
    private double longitud_nodo;
    private ArrayList<Conexion> conexiones;

    public Nodo(int id_nodo, int id_campus, double latitud_nodo,double longitud_nodo, ArrayList<Conexion> conexiones) {
        this.id_nodo = id_nodo;
        this.id_campus = id_campus;
        this.latitud_nodo = latitud_nodo;
        this.longitud_nodo = longitud_nodo;
        this.conexiones = conexiones;
    }

    public int getId_nodo() {
        return id_nodo;
    }

    public void setId_nodo(int id_nodo) {
        this.id_nodo = id_nodo;
    }

    public int getId_campus() {
        return id_campus;
    }

    public void setId_campus(int id_campus) {
        this.id_campus = id_campus;
    }

    public double getLatitud_nodo() {
        return latitud_nodo;
    }

    public void setLatitud_nodo(double latitud_nodo) {
        this.latitud_nodo = latitud_nodo;
    }

    public double getLongitud_nodo() {
        return longitud_nodo;
    }

    public void setLongitud_nodo(double longitud_nodo) {
        this.longitud_nodo = longitud_nodo;
    }

    public ArrayList<Conexion> getConexiones() {
        return conexiones;
    }

    public void setConexiones(ArrayList<Conexion> conexiones) {
        this.conexiones = conexiones;
    }
}
