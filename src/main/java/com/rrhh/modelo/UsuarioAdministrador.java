package com.rrhh.modelo;

import com.rrhh.modelo.enums.OpcionMenu;
import com.rrhh.modelo.enums.Permiso;
import com.rrhh.modelo.enums.TipoUsuario;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class UsuarioAdministrador extends Usuario {

    public UsuarioAdministrador(Integer id, String username, String passwordHash, Empleado empleado, boolean activo) {
        super(id, username, passwordHash, empleado, activo);
    }

    @Override
    public Set<Permiso> getPermisos() {
        return EnumSet.allOf(Permiso.class);
    }

    @Override
    public List<OpcionMenu> getMenuDisponible() {
        return List.of(OpcionMenu.values());
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.ADMINISTRADOR;
    }
}
