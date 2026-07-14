package com.rrhh.state;

import com.rrhh.modelo.Solicitud;

public class EstadoAprobada implements IEstadoSolicitud {

    @Override
    public void aprobar(Solicitud solicitud) {
        throw new TransicionInvalidaException("Una solicitud aprobada no puede volver a aprobarse");
    }

    @Override
    public void rechazar(Solicitud solicitud) {
        throw new TransicionInvalidaException("Una solicitud aprobada no puede rechazarse");
    }

    @Override
    public void cancelar(Solicitud solicitud) {
        solicitud.setEstado(new EstadoCancelada());
        solicitud.notificarObservadores();
    }

    @Override
    public String getNombreEstado() {
        return "APROBADA";
    }
}
