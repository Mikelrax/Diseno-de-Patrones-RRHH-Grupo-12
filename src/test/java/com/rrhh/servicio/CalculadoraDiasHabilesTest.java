package com.rrhh.servicio;

import com.rrhh.interfaces.IProveedorFeriados;
import com.rrhh.modelo.DiaFeriado;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraDiasHabilesTest {

    private static class ProveedorFeriadosFalso implements IProveedorFeriados {
        private final Set<LocalDate> feriados;

        ProveedorFeriadosFalso(Set<LocalDate> feriados) {
            this.feriados = feriados;
        }

        @Override
        public List<DiaFeriado> obtenerFeriados(int anio, String paisIso) {
            return List.of();
        }

        @Override
        public boolean esFeriado(LocalDate fecha, String paisIso) {
            return feriados.contains(fecha);
        }
    }

    @Test
    void excluyeFinesDeSemana() {
        CalculadoraDiasHabiles calculadora = new CalculadoraDiasHabiles(new ProveedorFeriadosFalso(Set.of()));

        LocalDate lunes = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate domingo = lunes.plusDays(6);

        assertEquals(5, calculadora.diasHabilesEntre(lunes, domingo, "PE"));
    }

    @Test
    void excluyeAdemasLosFeriados() {
        LocalDate lunes = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate miercoles = lunes.plusDays(2);
        LocalDate domingo = lunes.plusDays(6);

        CalculadoraDiasHabiles calculadora =
                new CalculadoraDiasHabiles(new ProveedorFeriadosFalso(Set.of(miercoles)));

        assertEquals(4, calculadora.diasHabilesEntre(lunes, domingo, "PE"));
    }
}
