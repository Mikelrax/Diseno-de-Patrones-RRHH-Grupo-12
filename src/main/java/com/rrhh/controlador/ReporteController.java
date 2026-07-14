package com.rrhh.controlador;

import com.rrhh.dto.ReporteEmpleadoDTO;
import com.rrhh.dto.ReporteNominaDTO;
import com.rrhh.main.AppContext;
import com.rrhh.proxy.NominaServicioProxy;
import com.rrhh.servicio.EmpleadoServicio;
import com.rrhh.util.ExportadorJSON;
import com.rrhh.util.NavegadorVistas;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class ReporteController {

    @FXML
    private ComboBox<String> comboTipoReporte;
    @FXML
    private Label labelMensaje;

    private final EmpleadoServicio empleadoServicio = new EmpleadoServicio();
    private final ExportadorJSON exportadorJSON = new ExportadorJSON();

    @FXML
    private void initialize() {
        comboTipoReporte.getItems().addAll("Empleados", "Nóminas");
        comboTipoReporte.getSelectionModel().selectFirst();
    }

    @FXML
    private void onExportar() {
        try {
            if ("Empleados".equals(comboTipoReporte.getValue())) {
                exportarEmpleados();
            } else {
                exportarNominas();
            }
        } catch (Exception e) {
            mostrarMensaje("Error al exportar: " + e.getMessage(), true);
        }
    }

    private void exportarEmpleados() throws Exception {
        File archivo = elegirArchivo("reporte_empleados.json");
        if (archivo == null) {
            return;
        }
        List<ReporteEmpleadoDTO> reporte = empleadoServicio.listarTodos().stream()
                .map(ReporteEmpleadoDTO::desde).collect(Collectors.toList());
        exportadorJSON.exportar(reporte, archivo.toPath());
        mostrarMensaje("Exportado a " + archivo.getName(), false);
    }

    private void exportarNominas() throws Exception {
        File archivo = elegirArchivo("reporte_nominas.json");
        if (archivo == null) {
            return;
        }
        NominaServicioProxy nominaServicio = new NominaServicioProxy(AppContext.getUsuarioActual());
        List<ReporteNominaDTO> reporte = nominaServicio.listarTodas().stream()
                .map(ReporteNominaDTO::desde).collect(Collectors.toList());
        exportadorJSON.exportar(reporte, archivo.toPath());
        mostrarMensaje("Exportado a " + archivo.getName(), false);
    }

    private File elegirArchivo(String nombreSugerido) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName(nombreSugerido);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        return chooser.showSaveDialog(comboTipoReporte.getScene().getWindow());
    }

    @FXML
    private void onVolver() {
        NavegadorVistas.cambiarVista("/vista/MenuPrincipalView.fxml", "RRHH - Menú principal");
    }

    private void mostrarMensaje(String texto, boolean esError) {
        labelMensaje.setText(texto);
        labelMensaje.getStyleClass().removeAll("error-label", "exito-label");
        labelMensaje.getStyleClass().add(esError ? "error-label" : "exito-label");
    }
}
