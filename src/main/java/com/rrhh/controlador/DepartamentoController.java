package com.rrhh.controlador;

import com.rrhh.modelo.Departamento;
import com.rrhh.modelo.Empleado;
import com.rrhh.servicio.DepartamentoServicio;
import com.rrhh.servicio.EmpleadoServicio;
import com.rrhh.util.NavegadorVistas;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;

public class DepartamentoController {

    @FXML
    private TableView<Departamento> tablaDepartamentos;
    @FXML
    private TableColumn<Departamento, String> colNombre;
    @FXML
    private TableColumn<Departamento, String> colPresupuesto;
    @FXML
    private TableColumn<Departamento, String> colGerente;

    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoPresupuesto;
    @FXML
    private ComboBox<Empleado> comboGerente;
    @FXML
    private Label labelMensaje;

    private final DepartamentoServicio departamentoServicio = new DepartamentoServicio();
    private final EmpleadoServicio empleadoServicio = new EmpleadoServicio();

    private Departamento departamentoSeleccionado;

    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPresupuesto.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPresupuesto().toString()));
        colGerente.setCellValueFactory(data -> {
            Empleado gerente = data.getValue().getGerente();
            return new SimpleStringProperty(gerente != null ? gerente.getNombreCompleto() : "-");
        });

        tablaDepartamentos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) {
                cargarEnFormulario(seleccionado);
            }
        });

        comboGerente.setItems(FXCollections.observableArrayList(empleadoServicio.listarTodos()));
        cargarTabla();
    }

    private void cargarTabla() {
        tablaDepartamentos.setItems(FXCollections.observableArrayList(departamentoServicio.listarTodos()));
    }

    private void cargarEnFormulario(Departamento departamento) {
        departamentoSeleccionado = departamento;
        campoNombre.setText(departamento.getNombre());
        campoPresupuesto.setText(departamento.getPresupuesto().toString());
        comboGerente.setValue(departamento.getGerente());
    }

    @FXML
    private void onGuardar() {
        try {
            Departamento departamento = leerFormulario(null);
            departamentoServicio.registrar(departamento);
            mostrarMensaje("Departamento registrado correctamente", false);
            cargarTabla();
            onLimpiar();
        } catch (Exception e) {
            mostrarMensaje(e.getMessage(), true);
        }
    }

    @FXML
    private void onActualizar() {
        if (departamentoSeleccionado == null) {
            mostrarMensaje("Selecciona un departamento de la tabla primero", true);
            return;
        }
        try {
            Departamento departamento = leerFormulario(departamentoSeleccionado.getId());
            departamentoServicio.actualizar(departamento);
            mostrarMensaje("Departamento actualizado correctamente", false);
            cargarTabla();
            onLimpiar();
        } catch (Exception e) {
            mostrarMensaje(e.getMessage(), true);
        }
    }

    @FXML
    private void onEliminar() {
        if (departamentoSeleccionado == null) {
            mostrarMensaje("Selecciona un departamento de la tabla primero", true);
            return;
        }
        departamentoServicio.eliminar(departamentoSeleccionado.getId());
        mostrarMensaje("Departamento eliminado", false);
        cargarTabla();
        onLimpiar();
    }

    @FXML
    private void onLimpiar() {
        departamentoSeleccionado = null;
        campoNombre.clear();
        campoPresupuesto.clear();
        comboGerente.setValue(null);
        tablaDepartamentos.getSelectionModel().clearSelection();
    }

    @FXML
    private void onVolver() {
        NavegadorVistas.cambiarVista("/vista/MenuPrincipalView.fxml", "RRHH - Menú principal");
    }

    private Departamento leerFormulario(Integer id) {
        BigDecimal presupuesto = new BigDecimal(campoPresupuesto.getText().trim());
        return new Departamento(id, campoNombre.getText().trim(), presupuesto, comboGerente.getValue());
    }

    private void mostrarMensaje(String texto, boolean esError) {
        labelMensaje.setText(texto);
        labelMensaje.getStyleClass().removeAll("error-label", "exito-label");
        labelMensaje.getStyleClass().add(esError ? "error-label" : "exito-label");
    }
}
