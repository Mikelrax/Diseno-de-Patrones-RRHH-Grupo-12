package com.rrhh.servicio;

import com.rrhh.interfaces.IProveedorFeriados;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class CalculadoraDiasHabiles {

    private final IProveedorFeriados proveedorFeriados;

    public CalculadoraDiasHabiles(IProveedorFeriados proveedorFeriados) {
        this.proveedorFeriados = proveedorFeriados;
    }

    public int diasHabilesEntre(LocalDate inicio, LocalDate fin, String paisIso) {
        int dias = 0;
        for (LocalDate fecha = inicio; !fecha.isAfter(fin); fecha = fecha.plusDays(1)) {
            boolean esFinDeSemana = fecha.getDayOfWeek() == DayOfWeek.SATURDAY
                    || fecha.getDayOfWeek() == DayOfWeek.SUNDAY;
            if (!esFinDeSemana && !proveedorFeriados.esFeriado(fecha, paisIso)) {
                dias++;
            }
        }
        return dias;
    }
}
