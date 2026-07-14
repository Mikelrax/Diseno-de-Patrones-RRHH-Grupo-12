package com.rrhh.main;

import com.rrhh.modelo.Usuario;
import javafx.stage.Stage;

public final class AppContext {

    private static Stage stagePrincipal;
    private static Usuario usuarioActual;

    private AppContext() {
    }

    public static void setStagePrincipal(Stage stage) {
        stagePrincipal = stage;
    }

    public static Stage getStagePrincipal() {
        return stagePrincipal;
    }

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
