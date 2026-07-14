package com.rrhh.controlador;

import com.rrhh.facade.SolicitudFacade;
import com.rrhh.main.AppContext;
import com.rrhh.modelo.Solicitud;
import com.rrhh.servicio.SolicitudServicio;
import com.rrhh.util.NavegadorVistas;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class AprobacionController {

    @FXML
    private TableView<Solicitud> tablaSolicitudes;
    @FXML
    private TableColumn<Solicitud, String> colEmpleado;
    @FXML
    private TableColumn<Solicitud, String> colPeriodo;
    @FXML
    private TableColumn<Solicitud, String> colDias;
    @FXML
    private Label labelMensaje;

    private final SolicitudFacade solicitudFacade = new SolicitudFacade();
    private final SolicitudServicio solicitudServicio = new SolicitudServicio();

    @FXML
    private void initialize() {
        colEmpleado.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getEmpleado().getNombreCompleto()));
        colPeriodo.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getFechaInicio() + " a " + data.getValue().getFechaFin()));
        colDias.setCellValueFactory(data -> new SimpleStringProperty(
                String.valueOf(data.getValue().getDiasHabilesCalculados())));

        cargarTabla();
    }

    private void cargarTabla() {
        tablaSolicitudes.setItems(FXCollections.observableArrayList(solicitudServicio.listarPendientes()));
    }

    @FXML
    private void onAprobar() {
        Solicitud seleccionada = tablaSolicitudes.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Selecciona una solicitud de la tabla", true);
            return;
        }
        try {
            solicitudFacade.aprobar(seleccionada.getId(), AppContext.getUsuarioActual().getEmpleado());
            mostrarMensaje("Solicitud aprobada", false);
            cargarTabla();
        } catch (Exception e) {
            mostrarMensaje(e.getMessage(), true);
        }
    }

    @FXML
    private void onRechazar() {
        Solicitud seleccionada = tablaSolicitudes.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Selecciona una solicitud de la tabla", true);
            return;
        }
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setHeaderText("Motivo del rechazo");
        Optional<String> motivo = dialogo.showAndWait();
        if (motivo.isEmpty()) {
            return;
        }
        try {
            solicitudFacade.rechazar(seleccionada.getId(), AppContext.getUsuarioActual().getEmpleado(), motivo.get());
            mostrarMensaje("Solicitud rechazada", false);
            cargarTabla();
        } catch (Exception e) {
            mostrarMensaje(e.getMessage(), true);
        }
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
