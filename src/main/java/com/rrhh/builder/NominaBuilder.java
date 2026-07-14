package com.rrhh.builder;

import com.rrhh.modelo.DetalleNomina;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Nomina;
import com.rrhh.modelo.enums.TipoMovimientoNomina;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NominaBuilder {

    private final Nomina nomina;
    private Empleado empleado;
    private int mes;
    private int anio;
    private BigDecimal salarioBruto = BigDecimal.ZERO;

    public NominaBuilder() {
        this.nomina = new Nomina(null, null, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, LocalDateTime.now());
    }

    public NominaBuilder paraEmpleado(Empleado empleado) {
        this.empleado = empleado;
        return this;
    }

    public NominaBuilder periodo(int mes, int anio) {
        this.mes = mes;
        this.anio = anio;
        return this;
    }

    public NominaBuilder sueldoBase(BigDecimal salarioBruto) {
        this.salarioBruto = salarioBruto;
        return this;
    }

    public NominaBuilder agregarBonificacion(String concepto, BigDecimal monto) {
        nomina.agregarDetalle(new DetalleNomina(null, null, TipoMovimientoNomina.BONO, concepto, monto));
        return this;
    }

    public NominaBuilder agregarDeduccion(String concepto, BigDecimal monto) {
        nomina.agregarDetalle(new DetalleNomina(null, null, TipoMovimientoNomina.DEDUCCION, concepto, monto));
        return this;
    }

    public Nomina build() {
        if (empleado == null) {
            throw new IllegalStateException("La nómina requiere un empleado");
        }
        if (mes < 1 || mes > 12) {
            throw new IllegalStateException("Mes inválido: " + mes);
        }

        BigDecimal totalBonos = sumar(TipoMovimientoNomina.BONO);
        BigDecimal totalDeducciones = sumar(TipoMovimientoNomina.DEDUCCION);
        BigDecimal salarioNeto = salarioBruto.add(totalBonos).subtract(totalDeducciones);

        if (salarioNeto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("El salario neto no puede ser negativo");
        }

        nomina.setEmpleado(empleado);
        nomina.setMes(mes);
        nomina.setAnio(anio);
        nomina.setSalarioBruto(salarioBruto);
        nomina.setTotalBonificaciones(totalBonos);
        nomina.setTotalDeducciones(totalDeducciones);
        nomina.setSalarioNeto(salarioNeto);
        return nomina;
    }

    private BigDecimal sumar(TipoMovimientoNomina tipo) {
        return nomina.getDetalle().stream()
                .filter(item -> item.getTipo() == tipo)
                .map(DetalleNomina::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
