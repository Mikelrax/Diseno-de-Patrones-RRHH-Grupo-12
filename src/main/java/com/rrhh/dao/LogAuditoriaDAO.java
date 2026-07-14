package com.rrhh.dao;

import com.rrhh.singleton.GestorConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class LogAuditoriaDAO {

    public void registrar(Integer idUsuario, String accion, String entidadAfectada, String detalle) {
        String sql = "INSERT INTO logs_auditoria (id_usuario, accion, entidad_afectada, detalle) VALUES (?, ?, ?, ?)";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            if (idUsuario != null) {
                stmt.setInt(1, idUsuario);
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, accion);
            stmt.setString(3, entidadAfectada);
            stmt.setString(4, detalle);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar auditoría", e);
        }
    }
}
