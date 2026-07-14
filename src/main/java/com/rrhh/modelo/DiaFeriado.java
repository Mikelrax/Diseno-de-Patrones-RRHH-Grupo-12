package com.rrhh.modelo;

import java.time.LocalDate;

public class DiaFeriado {

    private LocalDate fecha;
    private String nombre;
    private boolean fijo;

    public DiaFeriado(LocalDate fecha, String nombre, boolean fijo) {
        this.fecha = fecha;
        this.nombre = nombre;
        this.fijo = fijo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isFijo() {
        return fijo;
    }

    public void setFijo(boolean fijo) {
        this.fijo = fijo;
    }
}
