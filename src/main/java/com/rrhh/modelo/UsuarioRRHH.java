package com.rrhh.modelo;

import com.rrhh.modelo.enums.OpcionMenu;
import com.rrhh.modelo.enums.Permiso;
import com.rrhh.modelo.enums.TipoUsuario;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class UsuarioRRHH extends Usuario {

    public UsuarioRRHH(Integer id, String username, String passwordHash, Empleado empleado, boolean activo) {
        super(id, username, passwordHash, empleado, activo);
    }

    @Override
    public Set<Permiso> getPermisos() {
        return EnumSet.of(
                Permiso.VER_EMPLEADOS, Permiso.GESTIONAR_EMPLEADOS,
                Permiso.VER_DEPARTAMENTOS, Permiso.GESTIONAR_DEPARTAMENTOS,
                Permiso.VER_NOMINA_TODOS, Permiso.GESTIONAR_NOMINA,
                Permiso.VER_SOLICITUDES_PROPIAS, Permiso.CREAR_SOLICITUDES, Permiso.APROBAR_SOLICITUDES,
                Permiso.VER_DASHBOARD, Permiso.GENERAR_REPORTES
        );
    }

    @Override
    public List<OpcionMenu> getMenuDisponible() {
        return List.of(OpcionMenu.EMPLEADOS, OpcionMenu.DEPARTAMENTOS, OpcionMenu.NOMINA,
                OpcionMenu.SOLICITUDES, OpcionMenu.APROBACIONES, OpcionMenu.DASHBOARD, OpcionMenu.REPORTES);
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.RRHH;
    }
}
