package com.rrhh.modelo;

import java.math.BigDecimal;

public class Cargo {

    private Integer id;
    private String nombre;
    private BigDecimal salarioMin;
    private BigDecimal salarioMax;

    public Cargo(Integer id, String nombre, BigDecimal salarioMin, BigDecimal salarioMax) {
        this.id = id;
        this.nombre = nombre;
        this.salarioMin = salarioMin;
        this.salarioMax = salarioMax;
    }

    public boolean admiteSalario(BigDecimal salario) {
        return salario.compareTo(salarioMin) >= 0 && salario.compareTo(salarioMax) <= 0;
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

    public BigDecimal getSalarioMin() {
        return salarioMin;
    }

    public void setSalarioMin(BigDecimal salarioMin) {
        this.salarioMin = salarioMin;
    }

    public BigDecimal getSalarioMax() {
        return salarioMax;
    }

    public void setSalarioMax(BigDecimal salarioMax) {
        this.salarioMax = salarioMax;
    }
}
