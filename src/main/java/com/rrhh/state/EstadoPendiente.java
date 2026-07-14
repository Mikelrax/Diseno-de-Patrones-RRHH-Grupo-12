package com.rrhh.state;

import com.rrhh.modelo.Solicitud;

import java.time.LocalDateTime;

public class EstadoPendiente implements IEstadoSolicitud {

    @Override
    public void aprobar(Solicitud solicitud) {
        solicitud.setEstado(new EstadoAprobada());
        solicitud.setFechaResolucion(LocalDateTime.now());
        solicitud.notificarObservadores();
    }

    @Override
    public void rechazar(Solicitud solicitud) {
        solicitud.setEstado(new EstadoRechazada());
        solicitud.setFechaResolucion(LocalDateTime.now());
        solicitud.notificarObservadores();
    }

    @Override
    public void cancelar(Solicitud solicitud) {
        solicitud.setEstado(new EstadoCancelada());
        solicitud.setFechaResolucion(LocalDateTime.now());
        solicitud.notificarObservadores();
    }

    @Override
    public String getNombreEstado() {
        return "PENDIENTE";
    }
}
