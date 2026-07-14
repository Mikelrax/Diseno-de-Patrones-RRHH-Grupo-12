package com.rrhh.modelo;

import com.rrhh.modelo.enums.OpcionMenu;
import com.rrhh.modelo.enums.Permiso;
import com.rrhh.modelo.enums.TipoUsuario;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class UsuarioEmpleado extends Usuario {

    public UsuarioEmpleado(Integer id, String username, String passwordHash, Empleado empleado, boolean activo) {
        super(id, username, passwordHash, empleado, activo);
    }

    @Override
    public Set<Permiso> getPermisos() {
        return EnumSet.of(
                Permiso.VER_NOMINA_PROPIA,
                Permiso.VER_SOLICITUDES_PROPIAS, Permiso.CREAR_SOLICITUDES
        );
    }

    @Override
    public List<OpcionMenu> getMenuDisponible() {
        return List.of(OpcionMenu.NOMINA, OpcionMenu.SOLICITUDES);
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.EMPLEADO;
    }
}
