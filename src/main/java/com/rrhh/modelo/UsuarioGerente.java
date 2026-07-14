package com.rrhh.modelo;

import com.rrhh.modelo.enums.OpcionMenu;
import com.rrhh.modelo.enums.Permiso;
import com.rrhh.modelo.enums.TipoUsuario;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class UsuarioGerente extends Usuario {

    public UsuarioGerente(Integer id, String username, String passwordHash, Empleado empleado, boolean activo) {
        super(id, username, passwordHash, empleado, activo);
    }

    @Override
    public Set<Permiso> getPermisos() {
        return EnumSet.of(
                Permiso.VER_EMPLEADOS, Permiso.VER_NOMINA_TODOS,
                Permiso.VER_SOLICITUDES_PROPIAS, Permiso.CREAR_SOLICITUDES, Permiso.APROBAR_SOLICITUDES,
                Permiso.VER_DASHBOARD
        );
    }

    @Override
    public List<OpcionMenu> getMenuDisponible() {
        return List.of(OpcionMenu.EMPLEADOS, OpcionMenu.SOLICITUDES, OpcionMenu.APROBACIONES, OpcionMenu.DASHBOARD);
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.GERENTE;
    }
}
