package com.rrhh.interfaces;

import com.rrhh.modelo.Solicitud;

import java.util.List;
import java.util.Optional;

public interface ISolicitudServicio {

    Optional<Solicitud> buscarPorId(Integer id);

    List<Solicitud> listarPorEmpleado(Integer idEmpleado);

    List<Solicitud> listarPendientes();
}
