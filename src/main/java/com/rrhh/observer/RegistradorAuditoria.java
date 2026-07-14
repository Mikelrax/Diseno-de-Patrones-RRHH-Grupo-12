package com.rrhh.observer;

import com.rrhh.dao.LogAuditoriaDAO;
import com.rrhh.modelo.Solicitud;

public class RegistradorAuditoria implements IObservador {

    private final LogAuditoriaDAO logAuditoriaDAO = new LogAuditoriaDAO();

    @Override
    public void actualizar(Solicitud solicitud) {
        String detalle = "Solicitud de " + solicitud.getEmpleado().getNombreCompleto()
                + " (" + solicitud.getTipoSolicitud() + ") -> " + solicitud.getNombreEstado();
        logAuditoriaDAO.registrar(null, "CAMBIO_ESTADO_SOLICITUD", "Solicitud#" + solicitud.getId(), detalle);
    }
}
