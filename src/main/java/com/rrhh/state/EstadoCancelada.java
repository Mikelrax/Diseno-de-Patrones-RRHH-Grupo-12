package com.rrhh.state;

import com.rrhh.modelo.Solicitud;

public class EstadoCancelada implements IEstadoSolicitud {

    @Override
    public void aprobar(Solicitud solicitud) {
        throw new TransicionInvalidaException("Una solicitud cancelada no puede aprobarse");
    }

    @Override
    public void rechazar(Solicitud solicitud) {
        throw new TransicionInvalidaException("Una solicitud cancelada no puede rechazarse");
    }

    @Override
    public void cancelar(Solicitud solicitud) {
        throw new TransicionInvalidaException("Una solicitud cancelada no puede volver a cancelarse");
    }

    @Override
    public String getNombreEstado() {
        return "CANCELADA";
    }
}
