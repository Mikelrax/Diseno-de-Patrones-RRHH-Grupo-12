package com.rrhh.controlador;

import com.rrhh.main.AppContext;
import com.rrhh.modelo.Departamento;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Nomina;
import com.rrhh.modelo.enums.EstadoEmpleado;
import com.rrhh.proxy.NominaServicioProxy;
import com.rrhh.servicio.DepartamentoServicio;
import com.rrhh.servicio.EmpleadoServicio;
import com.rrhh.servicio.SolicitudServicio;
import com.rrhh.util.NavegadorVistas;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML
    private PieChart chartEmpleadosPorDepartamento;
    @FXML
    private BarChart<String, Number> chartNominaPorMes;
    @FXML
    private Label labelTotalEmpleados;
    @FXML
    private Label labelTotalDepartamentos;
    @FXML
    private Label labelSolicitudesPendientes;

    private final EmpleadoServicio empleadoServicio = new EmpleadoServicio();
    private final DepartamentoServicio departamentoServicio = new DepartamentoServicio();
    private final SolicitudServicio solicitudServicio = new SolicitudServicio();

    @FXML
    private void initialize() {
        List<Empleado> empleados = empleadoServicio.listarTodos();
        List<Departamento> departamentos = departamentoServicio.listarTodos();

        long activos = empleados.stream().filter(e -> e.getEstado() == EstadoEmpleado.ACTIVO).count();
        labelTotalEmpleados.setText(String.valueOf(activos));
        labelTotalDepartamentos.setText(String.valueOf(departamentos.size()));
        labelSolicitudesPendientes.setText(String.valueOf(solicitudServicio.listarPendientes().size()));

        cargarGraficoDepartamentos(empleados);
        cargarGraficoNomina();
    }

    private void cargarGraficoDepartamentos(List<Empleado> empleados) {
        Map<String, Long> conteo = empleados.stream()
                .filter(e -> e.getDepartamento() != null)
                .collect(Collectors.groupingBy(e -> e.getDepartamento().getNombre(), LinkedHashMap::new,
                        Collectors.counting()));

        chartEmpleadosPorDepartamento.setData(FXCollections.observableArrayList(
                conteo.entrySet().stream()
                        .map(entrada -> new PieChart.Data(entrada.getKey(), entrada.getValue()))
                        .collect(Collectors.toList())));
    }

    private void cargarGraficoNomina() {
        NominaServicioProxy nominaServicio = new NominaServicioProxy(AppContext.getUsuarioActual());
        List<Nomina> nominas = nominaServicio.listarTodas();

        Map<String, BigDecimal> totalPorPeriodo = new LinkedHashMap<>();
        for (Nomina nomina : nominas) {
            String periodo = nomina.getMes() + "/" + nomina.getAnio();
            totalPorPeriodo.merge(periodo, nomina.getSalarioNeto(), BigDecimal::add);
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Nómina neta total");
        totalPorPeriodo.forEach((periodo, total) -> serie.getData().add(new XYChart.Data<>(periodo, total)));

        chartNominaPorMes.setData(FXCollections.observableArrayList(List.of(serie)));
    }

    @FXML
    private void onVolver() {
        NavegadorVistas.cambiarVista("/vista/MenuPrincipalView.fxml", "RRHH - Menú principal");
    }
}
