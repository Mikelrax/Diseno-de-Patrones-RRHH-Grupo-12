package com.rrhh.dao;

import com.rrhh.interfaces.IDao;
import com.rrhh.modelo.DetalleNomina;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Nomina;
import com.rrhh.modelo.enums.TipoMovimientoNomina;
import com.rrhh.singleton.GestorConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NominaDAO implements IDao<Nomina, Integer> {

    @Override
    public Nomina guardar(Nomina nomina) {
        String sql = "INSERT INTO nominas (id_empleado, mes, anio, salario_bruto, total_bonificaciones, "
                + "total_deducciones, salario_neto, fecha_generacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, nomina.getEmpleado().getId());
            stmt.setInt(2, nomina.getMes());
            stmt.setInt(3, nomina.getAnio());
            stmt.setBigDecimal(4, nomina.getSalarioBruto());
            stmt.setBigDecimal(5, nomina.getTotalBonificaciones());
            stmt.setBigDecimal(6, nomina.getTotalDeducciones());
            stmt.setBigDecimal(7, nomina.getSalarioNeto());
            stmt.setTimestamp(8, Timestamp.valueOf(nomina.getFechaGeneracion()));
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    nomina.setId(keys.getInt(1));
                }
            }
            guardarDetalle(conexion, nomina);
            return nomina;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar nómina", e);
        }
    }

    private void guardarDetalle(Connection conexion, Nomina nomina) throws SQLException {
        String sql = "INSERT INTO detalle_nomina (id_nomina, tipo, concepto, monto) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            for (DetalleNomina item : nomina.getDetalle()) {
                stmt.setInt(1, nomina.getId());
                stmt.setString(2, item.getTipo().name());
                stmt.setString(3, item.getConcepto());
                stmt.setBigDecimal(4, item.getMonto());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public Optional<Nomina> buscarPorId(Integer id) {
        String sql = "SELECT * FROM nominas WHERE id_nomina = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                Nomina nomina = mapear(rs);
                cargarDetalle(conexion, nomina);
                return Optional.of(nomina);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar nómina", e);
        }
    }

    public Optional<Nomina> buscarPorEmpleadoYPeriodo(Integer idEmpleado, int mes, int anio) {
        String sql = "SELECT * FROM nominas WHERE id_empleado = ? AND mes = ? AND anio = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            stmt.setInt(2, mes);
            stmt.setInt(3, anio);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                Nomina nomina = mapear(rs);
                cargarDetalle(conexion, nomina);
                return Optional.of(nomina);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar nómina por periodo", e);
        }
    }

    public List<Nomina> listarPorEmpleado(Integer idEmpleado) {
        String sql = "SELECT * FROM nominas WHERE id_empleado = ? ORDER BY anio DESC, mes DESC";
        List<Nomina> resultado = new ArrayList<>();
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Nomina nomina = mapear(rs);
                    cargarDetalle(conexion, nomina);
                    resultado.add(nomina);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar nóminas por empleado", e);
        }
        return resultado;
    }

    @Override
    public List<Nomina> listarTodos() {
        String sql = "SELECT * FROM nominas ORDER BY anio DESC, mes DESC";
        List<Nomina> resultado = new ArrayList<>();
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Nomina nomina = mapear(rs);
                cargarDetalle(conexion, nomina);
                resultado.add(nomina);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar nóminas", e);
        }
        return resultado;
    }

    @Override
    public void actualizar(Nomina nomina) {
        String sql = "UPDATE nominas SET salario_bruto = ?, total_bonificaciones = ?, total_deducciones = ?, "
                + "salario_neto = ? WHERE id_nomina = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setBigDecimal(1, nomina.getSalarioBruto());
            stmt.setBigDecimal(2, nomina.getTotalBonificaciones());
            stmt.setBigDecimal(3, nomina.getTotalDeducciones());
            stmt.setBigDecimal(4, nomina.getSalarioNeto());
            stmt.setInt(5, nomina.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar nómina", e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM nominas WHERE id_nomina = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar nómina", e);
        }
    }

    private void cargarDetalle(Connection conexion, Nomina nomina) throws SQLException {
        String sql = "SELECT * FROM detalle_nomina WHERE id_nomina = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, nomina.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    nomina.agregarDetalle(new DetalleNomina(
                            rs.getInt("id_detalle"),
                            nomina.getId(),
                            TipoMovimientoNomina.valueOf(rs.getString("tipo")),
                            rs.getString("concepto"),
                            rs.getBigDecimal("monto")
                    ));
                }
            }
        }
    }

    private Nomina mapear(ResultSet rs) throws SQLException {
        Empleado empleado = new EmpleadoDAO().buscarPorId(rs.getInt("id_empleado")).orElseThrow();
        return new Nomina(
                rs.getInt("id_nomina"),
                empleado,
                rs.getInt("mes"),
                rs.getInt("anio"),
                rs.getBigDecimal("salario_bruto"),
                rs.getBigDecimal("total_bonificaciones"),
                rs.getBigDecimal("total_deducciones"),
                rs.getBigDecimal("salario_neto"),
                rs.getTimestamp("fecha_generacion").toLocalDateTime()
        );
    }
}
