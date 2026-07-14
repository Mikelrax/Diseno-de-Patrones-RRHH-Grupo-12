package com.rrhh.observer;

import com.rrhh.modelo.Solicitud;

public class NotificadorEmpleadoSolicitante implements IObservador {

    @Override
    public void actualizar(Solicitud solicitud) {
        String destino = solicitud.getEmpleado().getEmail();
        String mensaje = "Tu solicitud de " + solicitud.getTipoSolicitud() + " ahora está " + solicitud.getNombreEstado();
        System.out.println("[Notificacion -> " + destino + "] " + mensaje);
    }
}
