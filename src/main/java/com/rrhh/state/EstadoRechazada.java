package com.rrhh.state;

import com.rrhh.modelo.Solicitud;

public class EstadoRechazada implements IEstadoSolicitud {

    @Override
    public void aprobar(Solicitud solicitud) {
        throw new TransicionInvalidaException("Una solicitud rechazada no puede aprobarse");
    }

    @Override
    public void rechazar(Solicitud solicitud) {
        throw new TransicionInvalidaException("Una solicitud rechazada no puede volver a rechazarse");
    }

    @Override
    public void cancelar(Solicitud solicitud) {
        throw new TransicionInvalidaException("Una solicitud rechazada no puede cancelarse");
    }

    @Override
    public String getNombreEstado() {
        return "RECHAZADA";
    }
}
