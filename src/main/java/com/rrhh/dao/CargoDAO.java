package com.rrhh.dao;

import com.rrhh.interfaces.IDao;
import com.rrhh.modelo.Cargo;
import com.rrhh.singleton.GestorConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CargoDAO implements IDao<Cargo, Integer> {

    @Override
    public Cargo guardar(Cargo cargo) {
        String sql = "INSERT INTO cargos (nombre, salario_min, salario_max) VALUES (?, ?, ?)";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cargo.getNombre());
            stmt.setBigDecimal(2, cargo.getSalarioMin());
            stmt.setBigDecimal(3, cargo.getSalarioMax());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    cargo.setId(keys.getInt(1));
                }
            }
            return cargo;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar cargo", e);
        }
    }

    @Override
    public Optional<Cargo> buscarPorId(Integer id) {
        String sql = "SELECT * FROM cargos WHERE id_cargo = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cargo", e);
        }
    }

    @Override
    public List<Cargo> listarTodos() {
        String sql = "SELECT * FROM cargos ORDER BY nombre";
        List<Cargo> resultado = new ArrayList<>();
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar cargos", e);
        }
        return resultado;
    }

    @Override
    public void actualizar(Cargo cargo) {
        String sql = "UPDATE cargos SET nombre = ?, salario_min = ?, salario_max = ? WHERE id_cargo = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, cargo.getNombre());
            stmt.setBigDecimal(2, cargo.getSalarioMin());
            stmt.setBigDecimal(3, cargo.getSalarioMax());
            stmt.setInt(4, cargo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cargo", e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM cargos WHERE id_cargo = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cargo", e);
        }
    }

    private Cargo mapear(ResultSet rs) throws SQLException {
        return new Cargo(
                rs.getInt("id_cargo"),
                rs.getString("nombre"),
                rs.getBigDecimal("salario_min"),
                rs.getBigDecimal("salario_max")
        );
    }
}
