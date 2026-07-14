package com.rrhh.singleton;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GestorConexionBDTest {

    @Test
    void conectaYLeeElUsuarioSemilla() throws SQLException {
        Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
        try (Statement statement = conexion.createStatement();
             ResultSet resultado = statement.executeQuery(
                     "SELECT username, tipo_usuario FROM usuarios WHERE username = 'admin'")) {
            assertTrue(resultado.next(), "Debe existir el usuario semilla 'admin'");
            assertEquals("ADMINISTRADOR", resultado.getString("tipo_usuario"));
        } finally {
            GestorConexionBD.obtenerInstancia().liberarConexion(conexion);
        }
    }
}
