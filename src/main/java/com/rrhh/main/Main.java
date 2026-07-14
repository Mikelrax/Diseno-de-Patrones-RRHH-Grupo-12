package com.rrhh.main;

import com.rrhh.util.NavegadorVistas;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        AppContext.setStagePrincipal(stage);
        NavegadorVistas.cambiarVista("/vista/LoginView.fxml", "RRHH - Iniciar sesión");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
