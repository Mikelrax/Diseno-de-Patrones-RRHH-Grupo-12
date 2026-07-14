package com.rrhh.controlador;

import com.rrhh.adapter.NagerDateAdapter;
import com.rrhh.facade.SolicitudFacade;
import com.rrhh.main.AppContext;
import com.rrhh.modelo.Solicitud;
import com.rrhh.servicio.CalculadoraDiasHabiles;
import com.rrhh.servicio.SolicitudServicio;
import com.rrhh.util.NavegadorVistas;
import com.rrhh.util.TareasFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;

public class SolicitudController {

    @FXML
    private DatePicker campoInicio;
    @FXML
    private DatePicker campoFin;
    @FXML
    private Label labelDiasHabiles;
    @FXML
    private Label labelMensaje;
    @FXML
    private TableView<Solicitud> tablaSolicitudes;
    @FXML
    private TableColumn<Solicitud, String> colPeriodo;
    @FXML
    private TableColumn<Solicitud, String> colDias;
    @FXML
    private TableColumn<Solicitud, String> colEstado;

    private final SolicitudFacade solicitudFacade = new SolicitudFacade();
    private final SolicitudServicio solicitudServicio = new SolicitudServicio();
    private final CalculadoraDiasHabiles calculadoraDiasHabiles = new CalculadoraDiasHabiles(new NagerDateAdapter());

    @FXML
    private void initialize() {
        colPeriodo.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getFechaInicio() + " a " + data.getValue().getFechaFin()));
        colDias.setCellValueFactory(data -> new SimpleStringProperty(
                String.valueOf(data.getValue().getDiasHabilesCalculados())));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreEstado()));

        cargarTabla();
    }

    private void cargarTabla() {
        Integer idEmpleado = AppContext.getUsuarioActual().getEmpleado().getId();
        TareasFX.ejecutar(
                () -> solicitudServicio.listarPorEmpleado(idEmpleado),
                lista -> tablaSolicitudes.setItems(FXCollections.observableArrayList(lista)),
                error -> mostrarMensaje("Error al cargar solicitudes: " + error.getMessage(), true));
    }

    @FXML
    private void onCalcularDias() {
        LocalDate inicio = campoInicio.getValue();
        LocalDate fin = campoFin.getValue();
        if (inicio == null || fin == null || fin.isBefore(inicio)) {
            labelDiasHabiles.setText("Selecciona un rango de fechas válido");
            return;
        }
        int dias = calculadoraDiasHabiles.diasHabilesEntre(inicio, fin, "PE");
        labelDiasHabiles.setText("Días hábiles: " + dias);
    }

    @FXML
    private void onEnviarSolicitud() {
        try {
            LocalDate inicio = campoInicio.getValue();
            LocalDate fin = campoFin.getValue();
            if (inicio == null || fin == null) {
                mostrarMensaje("Selecciona ambas fechas", true);
                return;
            }
            solicitudFacade.crearSolicitudVacaciones(AppContext.getUsuarioActual().getEmpleado(), inicio, fin, "PE");
            mostrarMensaje("Solicitud enviada correctamente", false);
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
