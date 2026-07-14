package com.rrhh.factory;

import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.UsuarioAdministrador;
import com.rrhh.modelo.UsuarioEmpleado;
import com.rrhh.modelo.enums.EstadoEmpleado;
import com.rrhh.modelo.enums.Permiso;
import com.rrhh.modelo.enums.TipoUsuario;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioFactoryTest {

    private Empleado nuevoEmpleado() {
        return new Empleado(1, "Luis", "Ramos", "87654321", "luis@rrhh.local",
                LocalDate.of(1990, 5, 10), LocalDate.of(2020, 1, 15), BigDecimal.valueOf(4000),
                EstadoEmpleado.ACTIVO, null, null);
    }

    @Test
    void creaLaSubclaseCorrectaSegunElTipo() {
        var admin = UsuarioFactory.crear(TipoUsuario.ADMINISTRADOR, 1, "admin", "hash", nuevoEmpleado(), true);
        var empleado = UsuarioFactory.crear(TipoUsuario.EMPLEADO, 2, "empleado", "hash", nuevoEmpleado(), true);

        assertInstanceOf(UsuarioAdministrador.class, admin);
        assertInstanceOf(UsuarioEmpleado.class, empleado);
        assertEquals(TipoUsuario.ADMINISTRADOR, admin.getTipoUsuario());
        assertEquals(TipoUsuario.EMPLEADO, empleado.getTipoUsuario());
    }

    @Test
    void elAdministradorTieneTodosLosPermisos() {
        var admin = UsuarioFactory.crear(TipoUsuario.ADMINISTRADOR, 1, "admin", "hash", nuevoEmpleado(), true);
        assertTrue(admin.getPermisos().containsAll(EnumSet.allOf(Permiso.class)));
    }

    @Test
    void crearNuevoHasheaLaPasswordPlana() {
        var usuario = UsuarioFactory.crearNuevo(TipoUsuario.EMPLEADO, "nuevo", "clave123", nuevoEmpleado());
        assertTrue(usuario.validarCredenciales("clave123"));
    }
}
