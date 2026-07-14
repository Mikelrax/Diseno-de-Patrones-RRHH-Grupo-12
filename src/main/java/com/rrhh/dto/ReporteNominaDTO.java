package com.rrhh.dto;

import com.rrhh.modelo.Nomina;

import java.math.BigDecimal;

public class ReporteNominaDTO {

    public String empleado;
    public int mes;
    public int anio;
    public BigDecimal salarioBruto;
    public BigDecimal totalBonificaciones;
    public BigDecimal totalDeducciones;
    public BigDecimal salarioNeto;

    public ReporteNominaDTO(String empleado, int mes, int anio, BigDecimal salarioBruto,
                             BigDecimal totalBonificaciones, BigDecimal totalDeducciones, BigDecimal salarioNeto) {
        this.empleado = empleado;
        this.mes = mes;
        this.anio = anio;
        this.salarioBruto = salarioBruto;
        this.totalBonificaciones = totalBonificaciones;
        this.totalDeducciones = totalDeducciones;
        this.salarioNeto = salarioNeto;
    }

    public static ReporteNominaDTO desde(Nomina nomina) {
        return new ReporteNominaDTO(
                nomina.getEmpleado().getNombreCompleto(),
                nomina.getMes(),
                nomina.getAnio(),
                nomina.getSalarioBruto(),
                nomina.getTotalBonificaciones(),
                nomina.getTotalDeducciones(),
                nomina.getSalarioNeto()
        );
    }
}
