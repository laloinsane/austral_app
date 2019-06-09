package com.example.laloinsane.austral_app.Models;

public class Conexion {
    private int destino;
    private double distancia;

    public Conexion(int destino, double distancia) {
        this.destino = destino;
        this.distancia = distancia;
    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
}
