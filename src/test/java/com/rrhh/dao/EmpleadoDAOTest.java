package com.rrhh.dao;

import com.rrhh.modelo.Cargo;
import com.rrhh.modelo.Departamento;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.enums.EstadoEmpleado;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmpleadoDAOTest {

    @Test
    void guardaYRecuperaUnEmpleadoConDepartamentoYCargo() {
        DepartamentoDAO departamentoDAO = new DepartamentoDAO();
        CargoDAO cargoDAO = new CargoDAO();
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();

        Departamento departamento = departamentoDAO.guardar(
                new Departamento(null, "Tecnología (test)", BigDecimal.valueOf(10000), null));
        Cargo cargo = cargoDAO.guardar(new Cargo(null, "Desarrollador (test)",
                BigDecimal.valueOf(2500), BigDecimal.valueOf(5000)));

        Empleado empleado = new Empleado(null, "Test", "Automatizado", "99999999",
                "test.automatizado@rrhh.local", LocalDate.of(1998, 4, 12), LocalDate.now(),
                BigDecimal.valueOf(3200), EstadoEmpleado.ACTIVO, departamento, cargo);
        empleadoDAO.guardar(empleado);

        try {
            Optional<Empleado> recuperado = empleadoDAO.buscarPorId(empleado.getId());
            assertTrue(recuperado.isPresent());
            assertEquals("Test", recuperado.get().getNombre());
            assertEquals("Tecnología (test)", recuperado.get().getDepartamento().getNombre());
            assertEquals("Desarrollador (test)", recuperado.get().getCargo().getNombre());
        } finally {
            empleadoDAO.eliminar(empleado.getId());
            cargoDAO.eliminar(cargo.getId());
            departamentoDAO.eliminar(departamento.getId());
        }
    }
}
