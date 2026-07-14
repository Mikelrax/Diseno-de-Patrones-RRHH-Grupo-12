package com.rrhh.dao;

import com.rrhh.interfaces.IDao;
import com.rrhh.modelo.Contrato;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.enums.EstadoContrato;
import com.rrhh.modelo.enums.TipoContrato;
import com.rrhh.singleton.GestorConexionBD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContratoDAO implements IDao<Contrato, Integer> {

    @Override
    public Contrato guardar(Contrato contrato) {
        String sql = "INSERT INTO contratos (id_empleado, tipo_contrato, fecha_inicio, fecha_fin, "
                + "salario_pactado, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, contrato.getEmpleado().getId());
            stmt.setString(2, contrato.getTipoContrato().name());
            stmt.setDate(3, Date.valueOf(contrato.getFechaInicio()));
            stmt.setDate(4, contrato.getFechaFin() != null ? Date.valueOf(contrato.getFechaFin()) : null);
            stmt.setBigDecimal(5, contrato.getSalarioPactado());
            stmt.setString(6, contrato.getEstado().name());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    contrato.setId(keys.getInt(1));
                }
            }
            return contrato;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar contrato", e);
        }
    }

    @Override
    public Optional<Contrato> buscarPorId(Integer id) {
        String sql = "SELECT * FROM contratos WHERE id_contrato = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar contrato", e);
        }
    }

    public List<Contrato> listarPorEmpleado(Integer idEmpleado) {
        String sql = "SELECT * FROM contratos WHERE id_empleado = ? ORDER BY fecha_inicio DESC";
        List<Contrato> resultado = new ArrayList<>();
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar contratos por empleado", e);
        }
        return resultado;
    }

    @Override
    public List<Contrato> listarTodos() {
        String sql = "SELECT * FROM contratos ORDER BY fecha_inicio DESC";
        List<Contrato> resultado = new ArrayList<>();
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar contratos", e);
        }
        return resultado;
    }

    @Override
    public void actualizar(Contrato contrato) {
        String sql = "UPDATE contratos SET tipo_contrato = ?, fecha_inicio = ?, fecha_fin = ?, "
                + "salario_pactado = ?, estado = ? WHERE id_contrato = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, contrato.getTipoContrato().name());
            stmt.setDate(2, Date.valueOf(contrato.getFechaInicio()));
            stmt.setDate(3, contrato.getFechaFin() != null ? Date.valueOf(contrato.getFechaFin()) : null);
            stmt.setBigDecimal(4, contrato.getSalarioPactado());
            stmt.setString(5, contrato.getEstado().name());
            stmt.setInt(6, contrato.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar contrato", e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM contratos WHERE id_contrato = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar contrato", e);
        }
    }

    private Contrato mapear(ResultSet rs) throws SQLException {
        Empleado empleado = new EmpleadoDAO().buscarPorId(rs.getInt("id_empleado")).orElseThrow();
        Date fechaFin = rs.getDate("fecha_fin");
        return new Contrato(
                rs.getInt("id_contrato"),
                empleado,
                TipoContrato.valueOf(rs.getString("tipo_contrato")),
                rs.getDate("fecha_inicio").toLocalDate(),
                fechaFin != null ? fechaFin.toLocalDate() : null,
                rs.getBigDecimal("salario_pactado"),
                EstadoContrato.valueOf(rs.getString("estado"))
        );
    }
}
