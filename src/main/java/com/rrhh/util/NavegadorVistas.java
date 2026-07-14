package com.rrhh.util;

import com.rrhh.main.AppContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class NavegadorVistas {

    private NavegadorVistas() {
    }

    public static void cambiarVista(String rutaFxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(NavegadorVistas.class.getResource(rutaFxml));
            Parent raiz = loader.load();
            Stage stage = AppContext.getStagePrincipal();
            if (stage.getScene() == null) {
                stage.setScene(new Scene(raiz, 1024, 680));
            } else {
                stage.getScene().setRoot(raiz);
            }
            stage.getScene().getStylesheets().setAll(
                    NavegadorVistas.class.getResource("/css/styles.css").toExternalForm());
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar la vista: " + rutaFxml, e);
        }
    }
}
