package com.rrhh.util;

import com.rrhh.singleton.GestorConexionBD;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class SchemaInitializer {

    private SchemaInitializer() {
    }

    public static void aplicar(String recursoSql) throws IOException, SQLException {
        String contenido = leerRecurso(recursoSql);
        String[] bloques = contenido.split(";");

        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             Statement statement = conexion.createStatement()) {
            for (String bloque : bloques) {
                String sql = limpiarComentarios(bloque).strip();
                if (sql.isEmpty()) {
                    continue;
                }
                statement.execute(sql);
            }
        }
    }

    private static String limpiarComentarios(String bloque) {
        StringBuilder resultado = new StringBuilder();
        for (String linea : bloque.split("\\R")) {
            if (!linea.strip().startsWith("--")) {
                resultado.append(linea).append('\n');
            }
        }
        return resultado.toString();
    }

    private static String leerRecurso(String recurso) throws IOException {
        try (InputStream input = SchemaInitializer.class.getClassLoader().getResourceAsStream(recurso)) {
            if (input == null) {
                throw new IllegalStateException("No se encontró el recurso " + recurso);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public static void main(String[] args) throws Exception {
        aplicar("db/schema.sql");
        System.out.println("Esquema aplicado correctamente.");
    }
}
