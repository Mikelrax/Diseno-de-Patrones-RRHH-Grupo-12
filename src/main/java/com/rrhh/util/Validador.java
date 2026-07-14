package com.rrhh.util;

import java.util.regex.Pattern;

public final class Validador {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern DNI_PATTERN = Pattern.compile("^\\d{8}$");

    private Validador() {
    }

    public static boolean esEmailValido(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean esDniValido(String dni) {
        return dni != null && DNI_PATTERN.matcher(dni).matches();
    }

    public static void requerido(Object valor, String campo) {
        boolean vacio = valor == null || (valor instanceof String cadena && cadena.isBlank());
        if (vacio) {
            throw new IllegalArgumentException(campo + " es obligatorio");
        }
    }
}
