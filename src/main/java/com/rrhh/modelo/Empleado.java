package com.rrhh.modelo;

import com.rrhh.modelo.enums.EstadoEmpleado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class Empleado extends Persona {

    private LocalDate fechaNacimiento;
    private LocalDate fechaContratacion;
    private BigDecimal salarioBase;
    private EstadoEmpleado estado;
    private Departamento departamento;
    private Cargo cargo;

    public Empleado(Integer id, String nombre, String apellido, String dni, String email,
                     LocalDate fechaNacimiento, LocalDate fechaContratacion, BigDecimal salarioBase,
                     EstadoEmpleado estado, Departamento departamento, Cargo cargo) {
        super(id, nombre, apellido, dni, email);
        this.fechaNacimiento = fechaNacimiento;
        this.fechaContratacion = fechaContratacion;
        this.salarioBase = salarioBase;
        this.estado = estado;
        this.departamento = departamento;
        this.cargo = cargo;
    }

    @Override
    public String getTipoPersona() {
        return "Empleado";
    }

    public int calcularAntiguedadAnios() {
        if (fechaContratacion == null) {
            return 0;
        }
        return Period.between(fechaContratacion, LocalDate.now()).getYears();
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(BigDecimal salarioBase) {
        this.salarioBase = salarioBase;
    }

    public EstadoEmpleado getEstado() {
        return estado;
    }

    public void setEstado(EstadoEmpleado estado) {
        this.estado = estado;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }
}
