package com.rrhh.facade;

import com.rrhh.dao.UsuarioDAO;
import com.rrhh.factory.UsuarioFactory;
import com.rrhh.interfaces.IEmpleadoServicio;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Usuario;
import com.rrhh.modelo.enums.TipoUsuario;
import com.rrhh.servicio.EmpleadoServicio;

public class ContratacionFacade {

    private final IEmpleadoServicio empleadoServicio;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public ContratacionFacade() {
        this(new EmpleadoServicio());
    }

    public ContratacionFacade(IEmpleadoServicio empleadoServicio) {
        this.empleadoServicio = empleadoServicio;
    }

    public Usuario contratarEmpleado(Empleado empleado, TipoUsuario tipoUsuario, String username,
                                      String passwordInicial) {
        empleadoServicio.registrar(empleado);

        Usuario usuario = UsuarioFactory.crearNuevo(tipoUsuario, username, passwordInicial, empleado);
        usuarioDAO.guardar(usuario);
        return usuario;
    }
}
