package com.rrhh.dto;

import com.rrhh.modelo.Empleado;

import java.math.BigDecimal;

public class ReporteEmpleadoDTO {

    public String nombreCompleto;
    public String dni;
    public String email;
    public String departamento;
    public String cargo;
    public BigDecimal salarioBase;
    public String estado;

    public ReporteEmpleadoDTO(String nombreCompleto, String dni, String email, String departamento,
                               String cargo, BigDecimal salarioBase, String estado) {
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.email = email;
        this.departamento = departamento;
        this.cargo = cargo;
        this.salarioBase = salarioBase;
        this.estado = estado;
    }

    public static ReporteEmpleadoDTO desde(Empleado empleado) {
        return new ReporteEmpleadoDTO(
                empleado.getNombreCompleto(),
                empleado.getDni(),
                empleado.getEmail(),
                empleado.getDepartamento() != null ? empleado.getDepartamento().getNombre() : null,
                empleado.getCargo() != null ? empleado.getCargo().getNombre() : null,
                empleado.getSalarioBase(),
                empleado.getEstado().name()
        );
    }
}
