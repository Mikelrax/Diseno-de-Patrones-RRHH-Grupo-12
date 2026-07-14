package com.rrhh.decorator;

import com.rrhh.modelo.Empleado;

import java.math.BigDecimal;

public class SalarioBase implements ICalculoSalario {

    private final Empleado empleado;

    public SalarioBase(Empleado empleado) {
        this.empleado = empleado;
    }

    @Override
    public BigDecimal calcular() {
        return empleado.getSalarioBase();
    }

    @Override
    public String getDescripcion() {
        return "Salario base";
    }
}
