package com.rrhh.dao;

import com.rrhh.modelo.Departamento;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.enums.EstadoEmpleado;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class DepartamentoDAOTest {

    @Test
    void departamentoConGerenteNoCausaRecursionInfinita() {
        DepartamentoDAO departamentoDAO = new DepartamentoDAO();
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();

        Departamento departamento = departamentoDAO.guardar(
                new Departamento(null, "Gerencia (test)", BigDecimal.valueOf(20000), null));

        Empleado gerente = new Empleado(null, "Jefa", "DePrueba", "88888888",
                "jefa.prueba@rrhh.local", LocalDate.of(1985, 1, 1), LocalDate.now(),
                BigDecimal.valueOf(6000), EstadoEmpleado.ACTIVO, departamento, null);
        empleadoDAO.guardar(gerente);

        departamento.setGerente(gerente);
        departamentoDAO.actualizar(departamento);

        try {
            Departamento recuperado = departamentoDAO.buscarPorId(departamento.getId()).orElseThrow();
            assertNotNull(recuperado.getGerente());
            assertEquals("Jefa", recuperado.getGerente().getNombre());
            assertNull(recuperado.getGerente().getDepartamento(),
                    "La carga del gerente debe ser superficial para evitar recursión infinita");
        } finally {
            empleadoDAO.eliminar(gerente.getId());
            departamentoDAO.eliminar(departamento.getId());
        }
    }
}
