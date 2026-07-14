package com.rrhh.state;

import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.SolicitudVacaciones;
import com.rrhh.modelo.enums.EstadoEmpleado;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EstadoSolicitudTest {

    private SolicitudVacaciones nuevaSolicitud() {
        Empleado empleado = new Empleado(1, "Ana", "Torres", "12345678", "ana@rrhh.local",
                LocalDate.of(1995, 1, 1), LocalDate.of(2022, 3, 1), BigDecimal.valueOf(3500),
                EstadoEmpleado.ACTIVO, null, null);
        return new SolicitudVacaciones(1, empleado, LocalDate.now(), LocalDate.now().plusDays(5), 5);
    }

    @Test
    void empiezaPendienteYSeAprueba() {
        SolicitudVacaciones solicitud = nuevaSolicitud();
        assertEquals("PENDIENTE", solicitud.getNombreEstado());

        solicitud.aprobar(solicitud.getEmpleado());

        assertEquals("APROBADA", solicitud.getNombreEstado());
    }

    @Test
    void noSePuedeAprobarUnaSolicitudYaAprobada() {
        SolicitudVacaciones solicitud = nuevaSolicitud();
        solicitud.aprobar(solicitud.getEmpleado());

        assertThrows(TransicionInvalidaException.class, () -> solicitud.aprobar(solicitud.getEmpleado()));
    }

    @Test
    void unaSolicitudRechazadaNoPuedeCancelarse() {
        SolicitudVacaciones solicitud = nuevaSolicitud();
        solicitud.rechazar(solicitud.getEmpleado(), "Periodo no disponible");

        assertEquals("RECHAZADA", solicitud.getNombreEstado());
        assertThrows(TransicionInvalidaException.class, solicitud::cancelar);
    }
}
