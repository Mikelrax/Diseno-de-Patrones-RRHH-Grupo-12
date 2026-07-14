package com.rrhh.modelo;

import java.time.LocalDate;

public class SolicitudVacaciones extends Solicitud {

    public SolicitudVacaciones(Integer id, Empleado empleado, LocalDate fechaInicio, LocalDate fechaFin,
                                int diasHabilesCalculados) {
        super(id, empleado, fechaInicio, fechaFin, diasHabilesCalculados);
    }

    @Override
    public String getTipoSolicitud() {
        return "Vacaciones";
    }
}
