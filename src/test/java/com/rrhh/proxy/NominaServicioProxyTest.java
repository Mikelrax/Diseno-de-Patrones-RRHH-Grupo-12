package com.rrhh.proxy;

import com.rrhh.dao.DepartamentoDAO;
import com.rrhh.dao.EmpleadoDAO;
import com.rrhh.modelo.Departamento;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.UsuarioEmpleado;
import com.rrhh.modelo.UsuarioRRHH;
import com.rrhh.modelo.enums.EstadoEmpleado;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NominaServicioProxyTest {

    @Test
    void unEmpleadoNoPuedeVerLaNominaDeOtro() {
        DepartamentoDAO departamentoDAO = new DepartamentoDAO();
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();

        Departamento departamento = departamentoDAO.guardar(
                new Departamento(null, "Proxy Test", BigDecimal.valueOf(5000), null));
        Empleado propietario = crearEmpleado(empleadoDAO, departamento, "77001122", "propietario.proxy@rrhh.local");
        Empleado curioso = crearEmpleado(empleadoDAO, departamento, "77003344", "curioso.proxy@rrhh.local");

        try {
            UsuarioEmpleado usuarioCurioso = new UsuarioEmpleado(null, "curioso.proxy", "hash", curioso, true);
            NominaServicioProxy proxy = new NominaServicioProxy(usuarioCurioso);

            assertThrows(AccesoDenegadoException.class, () -> proxy.historialDe(propietario));
        } finally {
            empleadoDAO.eliminar(propietario.getId());
            empleadoDAO.eliminar(curioso.getId());
            departamentoDAO.eliminar(departamento.getId());
        }
    }

    @Test
    void rrhhSiPuedeVerLaNominaDeCualquierEmpleado() {
        DepartamentoDAO departamentoDAO = new DepartamentoDAO();
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();

        Departamento departamento = departamentoDAO.guardar(
                new Departamento(null, "Proxy Test RRHH", BigDecimal.valueOf(5000), null));
        Empleado empleado = crearEmpleado(empleadoDAO, departamento, "77005566", "empleado.proxyrrhh@rrhh.local");
        Empleado empleadoRRHH = crearEmpleado(empleadoDAO, departamento, "77007788", "rrhh.proxyrrhh@rrhh.local");

        try {
            UsuarioRRHH usuarioRRHH = new UsuarioRRHH(null, "rrhh.proxy", "hash", empleadoRRHH, true);
            NominaServicioProxy proxy = new NominaServicioProxy(usuarioRRHH);

            assertNotNull(proxy.historialDe(empleado));
        } finally {
            empleadoDAO.eliminar(empleado.getId());
            empleadoDAO.eliminar(empleadoRRHH.getId());
            departamentoDAO.eliminar(departamento.getId());
        }
    }

    private Empleado crearEmpleado(EmpleadoDAO empleadoDAO, Departamento departamento, String dni, String email) {
        Empleado empleado = new Empleado(null, "Test", "Proxy", dni, email,
                LocalDate.of(1995, 1, 1), LocalDate.now(), BigDecimal.valueOf(2500),
                EstadoEmpleado.ACTIVO, departamento, null);
        return empleadoDAO.guardar(empleado);
    }
}
