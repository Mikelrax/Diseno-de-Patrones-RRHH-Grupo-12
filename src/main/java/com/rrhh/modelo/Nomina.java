package com.rrhh.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Nomina {

    private Integer id;
    private Empleado empleado;
    private int mes;
    private int anio;
    private BigDecimal salarioBruto;
    private BigDecimal totalBonificaciones;
    private BigDecimal totalDeducciones;
    private BigDecimal salarioNeto;
    private LocalDateTime fechaGeneracion;
    private List<DetalleNomina> detalle = new ArrayList<>();

    public Nomina(Integer id, Empleado empleado, int mes, int anio, BigDecimal salarioBruto,
                  BigDecimal totalBonificaciones, BigDecimal totalDeducciones, BigDecimal salarioNeto,
                  LocalDateTime fechaGeneracion) {
        this.id = id;
        this.empleado = empleado;
        this.mes = mes;
        this.anio = anio;
        this.salarioBruto = salarioBruto;
        this.totalBonificaciones = totalBonificaciones;
        this.totalDeducciones = totalDeducciones;
        this.salarioNeto = salarioNeto;
        this.fechaGeneracion = fechaGeneracion;
    }

    public void agregarDetalle(DetalleNomina item) {
        detalle.add(item);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public BigDecimal getSalarioBruto() {
        return salarioBruto;
    }

    public void setSalarioBruto(BigDecimal salarioBruto) {
        this.salarioBruto = salarioBruto;
    }

    public BigDecimal getTotalBonificaciones() {
        return totalBonificaciones;
    }

    public void setTotalBonificaciones(BigDecimal totalBonificaciones) {
        this.totalBonificaciones = totalBonificaciones;
    }

    public BigDecimal getTotalDeducciones() {
        return totalDeducciones;
    }

    public void setTotalDeducciones(BigDecimal totalDeducciones) {
        this.totalDeducciones = totalDeducciones;
    }

    public BigDecimal getSalarioNeto() {
        return salarioNeto;
    }

    public void setSalarioNeto(BigDecimal salarioNeto) {
        this.salarioNeto = salarioNeto;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public List<DetalleNomina> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleNomina> detalle) {
        this.detalle = detalle;
    }
}
