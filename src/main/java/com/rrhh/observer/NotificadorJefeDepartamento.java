package com.rrhh.observer;

import com.rrhh.modelo.Departamento;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Solicitud;

public class NotificadorJefeDepartamento implements IObservador {

    @Override
    public void actualizar(Solicitud solicitud) {
        Empleado empleado = solicitud.getEmpleado();
        Departamento departamento = empleado.getDepartamento();
        if (departamento == null || departamento.getGerente() == null) {
            return;
        }
        String destino = departamento.getGerente().getEmail();
        String mensaje = empleado.getNombreCompleto() + " tiene una solicitud " + solicitud.getNombreEstado();
        System.out.println("[Notificacion -> " + destino + "] " + mensaje);
    }
}
