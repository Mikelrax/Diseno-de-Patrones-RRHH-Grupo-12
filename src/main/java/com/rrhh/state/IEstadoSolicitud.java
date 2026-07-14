package com.rrhh.state;

import com.rrhh.modelo.Solicitud;

public interface IEstadoSolicitud {

    void aprobar(Solicitud solicitud);

    void rechazar(Solicitud solicitud);

    void cancelar(Solicitud solicitud);

    String getNombreEstado();

    static IEstadoSolicitud fromNombre(String nombre) {
        return switch (nombre) {
            case "PENDIENTE" -> new EstadoPendiente();
            case "APROBADA" -> new EstadoAprobada();
            case "RECHAZADA" -> new EstadoRechazada();
            case "CANCELADA" -> new EstadoCancelada();
            default -> throw new IllegalArgumentException("Estado desconocido: " + nombre);
        };
    }
}
