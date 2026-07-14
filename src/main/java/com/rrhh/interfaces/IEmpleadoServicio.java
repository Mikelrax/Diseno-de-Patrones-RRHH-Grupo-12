package com.rrhh.interfaces;

import com.rrhh.modelo.Empleado;

import java.util.List;
import java.util.Optional;

public interface IEmpleadoServicio {

    Empleado registrar(Empleado empleado);

    void actualizar(Empleado empleado);

    void eliminar(Integer id);

    Optional<Empleado> buscarPorId(Integer id);

    List<Empleado> listarTodos();
}
