package com.rrhh.modelo;

import java.math.BigDecimal;

public class Departamento {

    private Integer id;
    private String nombre;
    private BigDecimal presupuesto;
    private Empleado gerente;

    public Departamento(Integer id, String nombre, BigDecimal presupuesto, Empleado gerente) {
        this.id = id;
        this.nombre = nombre;
        this.presupuesto = presupuesto;
        this.gerente = gerente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Empleado getGerente() {
        return gerente;
    }

    public void setGerente(Empleado gerente) {
        this.gerente = gerente;
    }
}
