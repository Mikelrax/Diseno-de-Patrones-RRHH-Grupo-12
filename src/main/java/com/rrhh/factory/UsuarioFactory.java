package com.rrhh.factory;

import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Usuario;
import com.rrhh.modelo.UsuarioAdministrador;
import com.rrhh.modelo.UsuarioEmpleado;
import com.rrhh.modelo.UsuarioGerente;
import com.rrhh.modelo.UsuarioRRHH;
import com.rrhh.modelo.enums.TipoUsuario;
import com.rrhh.util.PasswordUtil;

public final class UsuarioFactory {

    private UsuarioFactory() {
    }

    public static Usuario crear(TipoUsuario tipo, Integer id, String username, String passwordHash,
                                 Empleado empleado, boolean activo) {
        return switch (tipo) {
            case ADMINISTRADOR -> new UsuarioAdministrador(id, username, passwordHash, empleado, activo);
            case RRHH -> new UsuarioRRHH(id, username, passwordHash, empleado, activo);
            case GERENTE -> new UsuarioGerente(id, username, passwordHash, empleado, activo);
            case EMPLEADO -> new UsuarioEmpleado(id, username, passwordHash, empleado, activo);
        };
    }

    public static Usuario crearNuevo(TipoUsuario tipo, String username, String passwordPlano, Empleado empleado) {
        return crear(tipo, null, username, PasswordUtil.hash(passwordPlano), empleado, true);
    }
}
