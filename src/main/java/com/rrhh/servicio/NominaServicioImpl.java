package com.rrhh.servicio;

import com.rrhh.builder.NominaBuilder;
import com.rrhh.dao.NominaDAO;
import com.rrhh.decorator.BonoAntiguedad;
import com.rrhh.decorator.BonoProductividad;
import com.rrhh.decorator.DeduccionImpuestoRenta;
import com.rrhh.decorator.DeduccionSeguroSocial;
import com.rrhh.decorator.DeduccionTardanzas;
import com.rrhh.decorator.ICalculoSalario;
import com.rrhh.decorator.SalarioBase;
import com.rrhh.interfaces.INominaServicio;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Nomina;

import java.math.BigDecimal;
import java.util.List;

public class NominaServicioImpl implements INominaServicio {

    private final NominaDAO nominaDAO = new NominaDAO();

    @Override
    public Nomina generarNominaDelMes(Empleado empleado, int mes, int anio, BigDecimal bonoProductividad,
                                       int minutosTardanza) {
        NominaBuilder builder = new NominaBuilder()
                .paraEmpleado(empleado)
                .periodo(mes, anio)
                .sueldoBase(empleado.getSalarioBase());

        ICalculoSalario calculo = new SalarioBase(empleado);

        if (bonoProductividad != null && bonoProductividad.compareTo(BigDecimal.ZERO) > 0) {
            BonoProductividad bono = new BonoProductividad(calculo, bonoProductividad);
            builder.agregarBonificacion(bono.getConcepto(), bono.getMonto());
            calculo = bono;
        }

        int antiguedad = empleado.calcularAntiguedadAnios();
        if (antiguedad > 0) {
            BonoAntiguedad bono = new BonoAntiguedad(calculo, antiguedad);
            builder.agregarBonificacion(bono.getConcepto(), bono.getMonto());
            calculo = bono;
        }

        if (minutosTardanza > 0) {
            DeduccionTardanzas deduccion = new DeduccionTardanzas(calculo, minutosTardanza);
            builder.agregarDeduccion(deduccion.getConcepto(), deduccion.getMonto());
            calculo = deduccion;
        }

        DeduccionSeguroSocial seguroSocial = new DeduccionSeguroSocial(calculo);
        builder.agregarDeduccion(seguroSocial.getConcepto(), seguroSocial.getMonto());
        calculo = seguroSocial;

        DeduccionImpuestoRenta impuestoRenta = new DeduccionImpuestoRenta(calculo);
        builder.agregarDeduccion(impuestoRenta.getConcepto(), impuestoRenta.getMonto());
        calculo = impuestoRenta;

        Nomina nomina = builder.build();
        if (calculo.calcular().compareTo(nomina.getSalarioNeto()) != 0) {
            throw new IllegalStateException("Inconsistencia entre el cálculo por decorador y el detalle de nómina");
        }
        return nominaDAO.guardar(nomina);
    }

    @Override
    public Nomina buscarNominaDelPeriodo(Empleado empleado, int mes, int anio) {
        return nominaDAO.buscarPorEmpleadoYPeriodo(empleado.getId(), mes, anio).orElse(null);
    }

    @Override
    public List<Nomina> historialDe(Empleado empleado) {
        return nominaDAO.listarPorEmpleado(empleado.getId());
    }

    @Override
    public List<Nomina> listarTodas() {
        return nominaDAO.listarTodos();
    }
}
