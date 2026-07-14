package com.rrhh.servicio;

import com.rrhh.dao.SolicitudDAO;
import com.rrhh.interfaces.ISolicitudServicio;
import com.rrhh.modelo.Solicitud;

import java.util.List;
import java.util.Optional;

public class SolicitudServicio implements ISolicitudServicio {

    private final SolicitudDAO solicitudDAO = new SolicitudDAO();

    @Override
    public Optional<Solicitud> buscarPorId(Integer id) {
        return solicitudDAO.buscarPorId(id);
    }

    @Override
    public List<Solicitud> listarPorEmpleado(Integer idEmpleado) {
        return solicitudDAO.listarPorEmpleado(idEmpleado);
    }

    @Override
    public List<Solicitud> listarPendientes() {
        return solicitudDAO.listarPendientes();
    }
}
