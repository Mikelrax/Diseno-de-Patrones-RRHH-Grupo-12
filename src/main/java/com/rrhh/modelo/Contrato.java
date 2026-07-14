package com.rrhh.modelo;

import com.rrhh.modelo.enums.EstadoContrato;
import com.rrhh.modelo.enums.TipoContrato;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Contrato {

    private Integer id;
    private Empleado empleado;
    private TipoContrato tipoContrato;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal salarioPactado;
    private EstadoContrato estado;

    public Contrato(Integer id, Empleado empleado, TipoContrato tipoContrato, LocalDate fechaInicio,
                     LocalDate fechaFin, BigDecimal salarioPactado, EstadoContrato estado) {
        this.id = id;
        this.empleado = empleado;
        this.tipoContrato = tipoContrato;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.salarioPactado = salarioPactado;
        this.estado = estado;
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

    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getSalarioPactado() {
        return salarioPactado;
    }

    public void setSalarioPactado(BigDecimal salarioPactado) {
        this.salarioPactado = salarioPactado;
    }

    public EstadoContrato getEstado() {
        return estado;
    }

    public void setEstado(EstadoContrato estado) {
        this.estado = estado;
    }
}
