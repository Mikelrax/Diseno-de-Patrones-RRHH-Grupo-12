package com.rrhh.servicio;

import com.rrhh.dao.EmpleadoDAO;
import com.rrhh.interfaces.IEmpleadoServicio;
import com.rrhh.modelo.Empleado;
import com.rrhh.util.Validador;

import java.util.List;
import java.util.Optional;

public class EmpleadoServicio implements IEmpleadoServicio {

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    @Override
    public Empleado registrar(Empleado empleado) {
        validar(empleado);
        if (empleadoDAO.buscarPorDni(empleado.getDni()).isPresent()) {
            throw new IllegalStateException("Ya existe un empleado con DNI " + empleado.getDni());
        }
        return empleadoDAO.guardar(empleado);
    }

    @Override
    public void actualizar(Empleado empleado) {
        validar(empleado);
        empleadoDAO.actualizar(empleado);
    }

    @Override
    public void eliminar(Integer id) {
        empleadoDAO.eliminar(id);
    }

    @Override
    public Optional<Empleado> buscarPorId(Integer id) {
        return empleadoDAO.buscarPorId(id);
    }

    @Override
    public List<Empleado> listarTodos() {
        return empleadoDAO.listarTodos();
    }

    private void validar(Empleado empleado) {
        Validador.requerido(empleado.getNombre(), "nombre");
        Validador.requerido(empleado.getApellido(), "apellido");
        if (!Validador.esDniValido(empleado.getDni())) {
            throw new IllegalArgumentException("DNI inválido: debe tener 8 dígitos");
        }
        if (!Validador.esEmailValido(empleado.getEmail())) {
            throw new IllegalArgumentException("Email inválido: " + empleado.getEmail());
        }
    }
}
