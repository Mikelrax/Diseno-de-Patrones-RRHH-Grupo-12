package com.rrhh.dao;

import com.rrhh.modelo.DiaFeriado;
import com.rrhh.singleton.GestorConexionBD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeriadoCacheDAO {

    public void guardarTodos(List<DiaFeriado> feriados, String pais) {
        String sql = "INSERT IGNORE INTO feriados_cache (fecha, nombre, pais, anio) VALUES (?, ?, ?, ?)";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            for (DiaFeriado feriado : feriados) {
                stmt.setDate(1, Date.valueOf(feriado.getFecha()));
                stmt.setString(2, feriado.getNombre());
                stmt.setString(3, pais);
                stmt.setInt(4, feriado.getFecha().getYear());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar feriados en caché", e);
        }
    }

    public List<DiaFeriado> listarPorAnioYPais(int anio, String pais) {
        String sql = "SELECT * FROM feriados_cache WHERE anio = ? AND pais = ? ORDER BY fecha";
        List<DiaFeriado> resultado = new ArrayList<>();
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, anio);
            stmt.setString(2, pais);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new DiaFeriado(rs.getDate("fecha").toLocalDate(), rs.getString("nombre"), false));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar feriados en caché", e);
        }
        return resultado;
    }

    public boolean existePara(int anio, String pais) {
        String sql = "SELECT COUNT(*) FROM feriados_cache WHERE anio = ? AND pais = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, anio);
            stmt.setString(2, pais);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar caché de feriados", e);
        }
    }
}
