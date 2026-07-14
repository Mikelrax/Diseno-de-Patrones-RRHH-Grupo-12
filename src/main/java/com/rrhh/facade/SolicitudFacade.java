package com.rrhh.facade;

import com.rrhh.adapter.NagerDateAdapter;
import com.rrhh.dao.SolicitudDAO;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Solicitud;
import com.rrhh.modelo.SolicitudVacaciones;
import com.rrhh.observer.NotificadorEmpleadoSolicitante;
import com.rrhh.observer.NotificadorJefeDepartamento;
import com.rrhh.observer.RegistradorAuditoria;
import com.rrhh.servicio.CalculadoraDiasHabiles;

import java.time.LocalDate;

public class SolicitudFacade {

    private final SolicitudDAO solicitudDAO = new SolicitudDAO();
    private final CalculadoraDiasHabiles calculadoraDiasHabiles;

    public SolicitudFacade() {
        this(new CalculadoraDiasHabiles(new NagerDateAdapter()));
    }

    public SolicitudFacade(CalculadoraDiasHabiles calculadoraDiasHabiles) {
        this.calculadoraDiasHabiles = calculadoraDiasHabiles;
    }

    public Solicitud crearSolicitudVacaciones(Empleado empleado, LocalDate inicio, LocalDate fin, String paisIso) {
        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior al inicio");
        }
        int diasHabiles = calculadoraDiasHabiles.diasHabilesEntre(inicio, fin, paisIso);
        SolicitudVacaciones solicitud = new SolicitudVacaciones(null, empleado, inicio, fin, diasHabiles);
        return solicitudDAO.guardar(solicitud);
    }

    public Solicitud aprobar(Integer idSolicitud, Empleado aprobador) {
        Solicitud solicitud = cargarConObservadores(idSolicitud);
        solicitud.aprobar(aprobador);
        solicitudDAO.actualizar(solicitud);
        return solicitud;
    }

    public Solicitud rechazar(Integer idSolicitud, Empleado aprobador, String motivo) {
        Solicitud solicitud = cargarConObservadores(idSolicitud);
        solicitud.rechazar(aprobador, motivo);
        solicitudDAO.actualizar(solicitud);
        return solicitud;
    }

    public Solicitud cancelar(Integer idSolicitud) {
        Solicitud solicitud = cargarConObservadores(idSolicitud);
        solicitud.cancelar();
        solicitudDAO.actualizar(solicitud);
        return solicitud;
    }

    private Solicitud cargarConObservadores(Integer idSolicitud) {
        Solicitud solicitud = solicitudDAO.buscarPorId(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + idSolicitud));
        solicitud.agregarObservador(new NotificadorEmpleadoSolicitante());
        solicitud.agregarObservador(new NotificadorJefeDepartamento());
        solicitud.agregarObservador(new RegistradorAuditoria());
        return solicitud;
    }
}
