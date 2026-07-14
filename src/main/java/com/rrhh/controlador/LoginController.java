package com.rrhh.controlador;

import com.rrhh.main.AppContext;
import com.rrhh.modelo.Usuario;
import com.rrhh.servicio.AutenticacionServicio;
import com.rrhh.util.NavegadorVistas;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Optional;

public class LoginController {

    @FXML
    private TextField campoUsername;
    @FXML
    private PasswordField campoPassword;
    @FXML
    private Label labelError;

    private final AutenticacionServicio autenticacionServicio = new AutenticacionServicio();

    @FXML
    private void onIngresar() {
        Optional<Usuario> usuario = autenticacionServicio.autenticar(
                campoUsername.getText(), campoPassword.getText());
        if (usuario.isEmpty()) {
            labelError.setText("Usuario o contraseña incorrectos");
            return;
        }

        AppContext.setUsuarioActual(usuario.get());
        NavegadorVistas.cambiarVista("/vista/MenuPrincipalView.fxml", "RRHH - Menú principal");
    }
}
