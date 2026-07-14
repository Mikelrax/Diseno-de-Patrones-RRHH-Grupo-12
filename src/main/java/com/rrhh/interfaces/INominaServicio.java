package com.rrhh.interfaces;

import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Nomina;

import java.math.BigDecimal;
import java.util.List;

public interface INominaServicio {

    Nomina generarNominaDelMes(Empleado empleado, int mes, int anio, BigDecimal bonoProductividad,
                                int minutosTardanza);

    Nomina buscarNominaDelPeriodo(Empleado empleado, int mes, int anio);

    List<Nomina> historialDe(Empleado empleado);
}
