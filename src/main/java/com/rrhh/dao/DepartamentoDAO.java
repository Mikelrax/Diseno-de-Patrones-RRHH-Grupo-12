package com.rrhh.dao;

import com.rrhh.interfaces.IDao;
import com.rrhh.modelo.Departamento;
import com.rrhh.modelo.Empleado;
import com.rrhh.singleton.GestorConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartamentoDAO implements IDao<Departamento, Integer> {

    @Override
    public Departamento guardar(Departamento departamento) {
        String sql = "INSERT INTO departamentos (nombre, presupuesto, id_gerente) VALUES (?, ?, ?)";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, departamento.getNombre());
            stmt.setBigDecimal(2, departamento.getPresupuesto());
            enlazarGerente(stmt, 3, departamento.getGerente());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    departamento.setId(keys.getInt(1));
                }
            }
            return departamento;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar departamento", e);
        }
    }

    @Override
    public Optional<Departamento> buscarPorId(Integer id) {
        String sql = "SELECT * FROM departamentos WHERE id_departamento = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar departamento", e);
        }
    }

    @Override
    public List<Departamento> listarTodos() {
        String sql = "SELECT * FROM departamentos ORDER BY nombre";
        List<Departamento> resultado = new ArrayList<>();
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar departamentos", e);
        }
        return resultado;
    }

    @Override
    public void actualizar(Departamento departamento) {
        String sql = "UPDATE departamentos SET nombre = ?, presupuesto = ?, id_gerente = ? WHERE id_departamento = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, departamento.getNombre());
            stmt.setBigDecimal(2, departamento.getPresupuesto());
            enlazarGerente(stmt, 3, departamento.getGerente());
            stmt.setInt(4, departamento.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar departamento", e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM departamentos WHERE id_departamento = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar departamento", e);
        }
    }

    private void enlazarGerente(PreparedStatement stmt, int indice, Empleado gerente) throws SQLException {
        if (gerente != null) {
            stmt.setInt(indice, gerente.getId());
        } else {
            stmt.setNull(indice, Types.INTEGER);
        }
    }

    private Departamento mapear(ResultSet rs) throws SQLException {
        Empleado gerente = null;
        int idGerente = rs.getInt("id_gerente");
        if (!rs.wasNull()) {
            gerente = new EmpleadoDAO().buscarBasicoPorId(idGerente).orElse(null);
        }
        return new Departamento(
                rs.getInt("id_departamento"),
                rs.getString("nombre"),
                rs.getBigDecimal("presupuesto"),
                gerente
        );
    }
}
