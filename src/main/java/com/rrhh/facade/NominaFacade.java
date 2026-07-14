package com.rrhh.facade;

import com.rrhh.dao.EmpleadoDAO;
import com.rrhh.interfaces.INominaServicio;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Nomina;
import com.rrhh.modelo.enums.EstadoEmpleado;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NominaFacade {

    private final INominaServicio nominaServicio;
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    public NominaFacade(INominaServicio nominaServicio) {
        this.nominaServicio = nominaServicio;
    }

    public List<Nomina> generarNominaDelMesParaTodos(int mes, int anio) {
        List<Nomina> generadas = new ArrayList<>();
        for (Empleado empleado : empleadoDAO.listarTodos()) {
            if (empleado.getEstado() != EstadoEmpleado.ACTIVO) {
                continue;
            }
            generadas.add(nominaServicio.generarNominaDelMes(empleado, mes, anio, BigDecimal.ZERO, 0));
        }
        return generadas;
    }
}
