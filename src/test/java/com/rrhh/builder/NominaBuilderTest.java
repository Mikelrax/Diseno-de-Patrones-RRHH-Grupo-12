package com.rrhh.builder;

import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Nomina;
import com.rrhh.modelo.enums.EstadoEmpleado;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NominaBuilderTest {

    private Empleado nuevoEmpleado() {
        return new Empleado(1, "Carla", "Vega", "11223344", "carla@rrhh.local",
                LocalDate.of(1992, 3, 20), LocalDate.of(2019, 6, 1), BigDecimal.valueOf(3000),
                EstadoEmpleado.ACTIVO, null, null);
    }

    @Test
    void calculaElSalarioNetoConBonosYDeducciones() {
        Nomina nomina = new NominaBuilder()
                .paraEmpleado(nuevoEmpleado())
                .periodo(7, 2026)
                .sueldoBase(BigDecimal.valueOf(3000))
                .agregarBonificacion("Productividad", BigDecimal.valueOf(300))
                .agregarDeduccion("Seguro social", BigDecimal.valueOf(270))
                .build();

        assertEquals(0, nomina.getSalarioNeto().compareTo(BigDecimal.valueOf(3030)));
        assertEquals(2, nomina.getDetalle().size());
    }

    @Test
    void rechazaUnMesInvalido() {
        NominaBuilder builder = new NominaBuilder()
                .paraEmpleado(nuevoEmpleado())
                .periodo(13, 2026)
                .sueldoBase(BigDecimal.valueOf(3000));

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void rechazaUnSalarioNetoNegativo() {
        NominaBuilder builder = new NominaBuilder()
                .paraEmpleado(nuevoEmpleado())
                .periodo(7, 2026)
                .sueldoBase(BigDecimal.valueOf(100))
                .agregarDeduccion("Descuento excesivo", BigDecimal.valueOf(500));

        assertThrows(IllegalStateException.class, builder::build);
    }
}
