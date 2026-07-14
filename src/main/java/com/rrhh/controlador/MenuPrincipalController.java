package com.rrhh.controlador;

import com.rrhh.main.AppContext;
import com.rrhh.modelo.Usuario;
import com.rrhh.modelo.enums.OpcionMenu;
import com.rrhh.util.NavegadorVistas;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuPrincipalController {

    @FXML
    private VBox menuLateral;
    @FXML
    private Label labelUsuario;
    @FXML
    private Label labelBienvenida;

    @FXML
    private void initialize() {
        Usuario usuario = AppContext.getUsuarioActual();
        labelUsuario.setText(usuario.getUsername() + " (" + usuario.getTipoUsuario() + ")");
        labelBienvenida.setText(usuario.getEmpleado().getNombreCompleto());

        for (OpcionMenu opcion : usuario.getMenuDisponible()) {
            Button boton = new Button(etiquetaDe(opcion));
            boton.setMaxWidth(Double.MAX_VALUE);
            boton.setOnAction(event -> NavegadorVistas.cambiarVista(vistaDe(opcion), etiquetaDe(opcion)));
            menuLateral.getChildren().add(boton);
        }
    }

    @FXML
    private void onCerrarSesion() {
        AppContext.cerrarSesion();
        NavegadorVistas.cambiarVista("/vista/LoginView.fxml", "RRHH - Iniciar sesión");
    }

    private String etiquetaDe(OpcionMenu opcion) {
        return switch (opcion) {
            case EMPLEADOS -> "Empleados";
            case DEPARTAMENTOS -> "Departamentos";
            case NOMINA -> "Nómina";
            case SOLICITUDES -> "Mis solicitudes";
            case APROBACIONES -> "Aprobaciones";
            case DASHBOARD -> "Dashboard";
            case REPORTES -> "Reportes";
        };
    }

    private String vistaDe(OpcionMenu opcion) {
        return switch (opcion) {
            case EMPLEADOS -> "/vista/EmpleadoView.fxml";
            case DEPARTAMENTOS -> "/vista/DepartamentoView.fxml";
            case NOMINA -> "/vista/NominaView.fxml";
            case SOLICITUDES -> "/vista/SolicitudVacacionesView.fxml";
            case APROBACIONES -> "/vista/AprobacionSolicitudesView.fxml";
            case DASHBOARD -> "/vista/DashboardView.fxml";
            case REPORTES -> "/vista/ReportesView.fxml";
        };
    }
}
