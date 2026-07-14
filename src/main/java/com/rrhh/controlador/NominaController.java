package com.rrhh.controlador;

import com.rrhh.dto.ReporteNominaDTO;
import com.rrhh.main.AppContext;
import com.rrhh.modelo.DetalleNomina;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Nomina;
import com.rrhh.modelo.enums.Permiso;
import com.rrhh.proxy.NominaServicioProxy;
import com.rrhh.servicio.EmpleadoServicio;
import com.rrhh.util.ExportadorJSON;
import com.rrhh.util.NavegadorVistas;
import com.rrhh.util.TareasFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NominaController {

    @FXML
    private ComboBox<Empleado> comboEmpleado;
    @FXML
    private ComboBox<Integer> comboMes;
    @FXML
    private TextField campoAnio;
    @FXML
    private TextField campoBonoProductividad;
    @FXML
    private TextField campoMinutosTardanza;
    @FXML
    private TableView<DetalleNomina> tablaDetalle;
    @FXML
    private TableColumn<DetalleNomina, String> colConcepto;
    @FXML
    private TableColumn<DetalleNomina, String> colTipo;
    @FXML
    private TableColumn<DetalleNomina, String> colMonto;
    @FXML
    private Label labelBruto;
    @FXML
    private Label labelNeto;
    @FXML
    private Label labelMensaje;

    private final EmpleadoServicio empleadoServicio = new EmpleadoServicio();
    private NominaServicioProxy nominaServicio;

    @FXML
    private void initialize() {
        nominaServicio = new NominaServicioProxy(AppContext.getUsuarioActual());

        comboMes.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList())));
        campoAnio.setText(String.valueOf(LocalDate.now().getYear()));

        colConcepto.setCellValueFactory(new PropertyValueFactory<>("concepto"));
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipo().name()));
        colMonto.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMonto().toString()));

        cargarComboEmpleados();
    }

    private void cargarComboEmpleados() {
        TareasFX.ejecutar(
                empleadoServicio::listarTodos,
                lista -> {
                    comboEmpleado.setItems(FXCollections.observableArrayList(lista));
                    boolean puedeGenerar = AppContext.getUsuarioActual().tienePermiso(Permiso.GESTIONAR_NOMINA);
                    if (!puedeGenerar) {
                        comboEmpleado.setValue(AppContext.getUsuarioActual().getEmpleado());
                        comboEmpleado.setDisable(true);
                    }
                },
                error -> mostrarMensaje("Error al cargar empleados: " + error.getMessage(), true));
    }

    @FXML
    private void onGenerar() {
        try {
            Empleado empleado = comboEmpleado.getValue();
            int mes = comboMes.getValue();
            int anio = Integer.parseInt(campoAnio.getText().trim());
            BigDecimal bono = campoBonoProductividad.getText().isBlank()
                    ? BigDecimal.ZERO : new BigDecimal(campoBonoProductividad.getText().trim());
            int tardanza = campoMinutosTardanza.getText().isBlank()
                    ? 0 : Integer.parseInt(campoMinutosTardanza.getText().trim());

            Nomina nomina = nominaServicio.generarNominaDelMes(empleado, mes, anio, bono, tardanza);
            mostrarNomina(nomina);
            mostrarMensaje("Nómina generada correctamente", false);
        } catch (Exception e) {
            mostrarMensaje(e.getMessage(), true);
        }
    }

    @FXML
    private void onExportar() {
        Empleado empleado = comboEmpleado.getValue();
        if (empleado == null) {
            mostrarMensaje("Selecciona un empleado primero", true);
            return;
        }
        List<Nomina> historial = nominaServicio.historialDe(empleado);
        List<ReporteNominaDTO> reporte = historial.stream().map(ReporteNominaDTO::desde).collect(Collectors.toList());

        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName("nomina_" + empleado.getDni() + ".json");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File archivo = chooser.showSaveDialog(tablaDetalle.getScene().getWindow());
        if (archivo == null) {
            return;
        }
        try {
            new ExportadorJSON().exportar(reporte, archivo.toPath());
            mostrarMensaje("Exportado a " + archivo.getName(), false);
        } catch (Exception e) {
            mostrarMensaje("Error al exportar: " + e.getMessage(), true);
        }
    }

    @FXML
    private void onVolver() {
        NavegadorVistas.cambiarVista("/vista/MenuPrincipalView.fxml", "RRHH - Menú principal");
    }

    private void mostrarNomina(Nomina nomina) {
        tablaDetalle.setItems(FXCollections.observableArrayList(nomina.getDetalle()));
        labelBruto.setText("Bruto: " + nomina.getSalarioBruto());
        labelNeto.setText("Neto: " + nomina.getSalarioNeto());
    }

    private void mostrarMensaje(String texto, boolean esError) {
        labelMensaje.setText(texto);
        labelMensaje.getStyleClass().removeAll("error-label", "exito-label");
        labelMensaje.getStyleClass().add(esError ? "error-label" : "exito-label");
    }
}
