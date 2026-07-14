package com.rrhh.decorator;

import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.enums.EstadoEmpleado;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecoratorCalculoTest {

    private Empleado nuevoEmpleado() {
        return new Empleado(1, "Marco", "Diaz", "55667788", "marco@rrhh.local",
                LocalDate.of(1993, 6, 15), LocalDate.of(2021, 1, 1), BigDecimal.valueOf(2000),
                EstadoEmpleado.ACTIVO, null, null);
    }

    @Test
    void aplicaBonoYDeduccionEnCadena() {
        ICalculoSalario calculo = new SalarioBase(nuevoEmpleado());
        calculo = new BonoProductividad(calculo, BigDecimal.valueOf(200));
        calculo = new DeduccionSeguroSocial(calculo);

        BigDecimal esperado = BigDecimal.valueOf(2000).add(BigDecimal.valueOf(200));
        BigDecimal seguroSocial = esperado.multiply(BigDecimal.valueOf(0.09));
        esperado = esperado.subtract(seguroSocial);

        assertEquals(0, esperado.compareTo(calculo.calcular()));
    }

    @Test
    void elOrdenDeLosDecoradoresAfectaElResultado() {
        Empleado empleado = nuevoEmpleado();

        ICalculoSalario primero = new DeduccionSeguroSocial(new SalarioBase(empleado));
        ICalculoSalario segundo = new BonoProductividad(
                new DeduccionSeguroSocial(new SalarioBase(empleado)), BigDecimal.valueOf(200));

        assertEquals(0, segundo.calcular().subtract(primero.calcular()).compareTo(BigDecimal.valueOf(200)));
    }
}
