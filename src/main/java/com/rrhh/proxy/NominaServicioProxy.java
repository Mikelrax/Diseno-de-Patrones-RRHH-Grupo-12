package com.rrhh.proxy;

import com.rrhh.dao.LogAuditoriaDAO;
import com.rrhh.interfaces.INominaServicio;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Nomina;
import com.rrhh.modelo.Usuario;
import com.rrhh.modelo.enums.Permiso;
import com.rrhh.servicio.NominaServicioImpl;

import java.math.BigDecimal;
import java.util.List;

public class NominaServicioProxy implements INominaServicio {

    private final INominaServicio servicioReal = new NominaServicioImpl();
    private final LogAuditoriaDAO logAuditoriaDAO = new LogAuditoriaDAO();
    private final Usuario usuarioActual;

    public NominaServicioProxy(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    @Override
    public Nomina generarNominaDelMes(Empleado empleado, int mes, int anio, BigDecimal bonoProductividad,
                                       int minutosTardanza) {
        exigirPermiso(Permiso.GESTIONAR_NOMINA);
        return servicioReal.generarNominaDelMes(empleado, mes, anio, bonoProductividad, minutosTardanza);
    }

    @Override
    public Nomina buscarNominaDelPeriodo(Empleado empleado, int mes, int anio) {
        exigirAccesoANomina(empleado);
        return servicioReal.buscarNominaDelPeriodo(empleado, mes, anio);
    }

    @Override
    public List<Nomina> historialDe(Empleado empleado) {
        exigirAccesoANomina(empleado);
        return servicioReal.historialDe(empleado);
    }

    private void exigirAccesoANomina(Empleado empleado) {
        boolean esPropia = usuarioActual.getEmpleado().getId().equals(empleado.getId());
        boolean puedeVerTodas = usuarioActual.tienePermiso(Permiso.VER_NOMINA_TODOS);
        if (!esPropia && !puedeVerTodas) {
            registrarIntentoDenegado(empleado);
            throw new AccesoDenegadoException(
                    "El usuario " + usuarioActual.getUsername() + " no puede ver la nómina de otro empleado");
        }
    }

    private void exigirPermiso(Permiso permiso) {
        if (!usuarioActual.tienePermiso(permiso)) {
            throw new AccesoDenegadoException(
                    "El usuario " + usuarioActual.getUsername() + " no tiene el permiso " + permiso);
        }
    }

    private void registrarIntentoDenegado(Empleado empleado) {
        logAuditoriaDAO.registrar(usuarioActual.getId(), "ACCESO_DENEGADO_NOMINA",
                "Empleado#" + empleado.getId(),
                usuarioActual.getUsername() + " intentó ver la nómina de " + empleado.getNombreCompleto());
    }
}
