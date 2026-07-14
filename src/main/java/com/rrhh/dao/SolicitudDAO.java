package com.rrhh.dao;

import com.rrhh.interfaces.IDao;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Solicitud;
import com.rrhh.modelo.SolicitudVacaciones;
import com.rrhh.singleton.GestorConexionBD;
import com.rrhh.state.IEstadoSolicitud;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SolicitudDAO implements IDao<Solicitud, Integer> {

    @Override
    public Solicitud guardar(Solicitud solicitud) {
        String sql = "INSERT INTO solicitudes (id_empleado, fecha_inicio, fecha_fin, dias_habiles_calculados, "
                + "estado, id_aprobador, fecha_solicitud, fecha_resolucion, comentario) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            enlazarParametros(stmt, solicitud);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    solicitud.setId(keys.getInt(1));
                }
            }
            return solicitud;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar solicitud", e);
        }
    }

    @Override
    public Optional<Solicitud> buscarPorId(Integer id) {
        String sql = "SELECT * FROM solicitudes WHERE id_solicitud = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar solicitud", e);
        }
    }

    public List<Solicitud> listarPorEmpleado(Integer idEmpleado) {
        String sql = "SELECT * FROM solicitudes WHERE id_empleado = ? ORDER BY fecha_solicitud DESC";
        List<Solicitud> resultado = new ArrayList<>();
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar solicitudes por empleado", e);
        }
        return resultado;
    }

    public List<Solicitud> listarPendientes() {
        String sql = "SELECT * FROM solicitudes WHERE estado = 'PENDIENTE' ORDER BY fecha_solicitud";
        List<Solicitud> resultado = new ArrayList<>();
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar solicitudes pendientes", e);
        }
        return resultado;
    }

    @Override
    public List<Solicitud> listarTodos() {
        String sql = "SELECT * FROM solicitudes ORDER BY fecha_solicitud DESC";
        List<Solicitud> resultado = new ArrayList<>();
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar solicitudes", e);
        }
        return resultado;
    }

    @Override
    public void actualizar(Solicitud solicitud) {
        String sql = "UPDATE solicitudes SET estado = ?, id_aprobador = ?, fecha_resolucion = ?, comentario = ? "
                + "WHERE id_solicitud = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, solicitud.getNombreEstado());
            if (solicitud.getAprobador() != null) {
                stmt.setInt(2, solicitud.getAprobador().getId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setTimestamp(3, solicitud.getFechaResolucion() != null
                    ? Timestamp.valueOf(solicitud.getFechaResolucion()) : null);
            stmt.setString(4, solicitud.getComentario());
            stmt.setInt(5, solicitud.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar solicitud", e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM solicitudes WHERE id_solicitud = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar solicitud", e);
        }
    }

    private void enlazarParametros(PreparedStatement stmt, Solicitud solicitud) throws SQLException {
        stmt.setInt(1, solicitud.getEmpleado().getId());
        stmt.setDate(2, Date.valueOf(solicitud.getFechaInicio()));
        stmt.setDate(3, Date.valueOf(solicitud.getFechaFin()));
        stmt.setInt(4, solicitud.getDiasHabilesCalculados());
        stmt.setString(5, solicitud.getNombreEstado());
        if (solicitud.getAprobador() != null) {
            stmt.setInt(6, solicitud.getAprobador().getId());
        } else {
            stmt.setNull(6, java.sql.Types.INTEGER);
        }
        stmt.setTimestamp(7, Timestamp.valueOf(solicitud.getFechaSolicitud()));
        stmt.setTimestamp(8, solicitud.getFechaResolucion() != null
                ? Timestamp.valueOf(solicitud.getFechaResolucion()) : null);
        stmt.setString(9, solicitud.getComentario());
    }

    private Solicitud mapear(ResultSet rs) throws SQLException {
        Empleado empleado = new EmpleadoDAO().buscarPorId(rs.getInt("id_empleado")).orElseThrow();
        SolicitudVacaciones solicitud = new SolicitudVacaciones(
                rs.getInt("id_solicitud"),
                empleado,
                rs.getDate("fecha_inicio").toLocalDate(),
                rs.getDate("fecha_fin").toLocalDate(),
                rs.getInt("dias_habiles_calculados")
        );
        solicitud.setEstado(IEstadoSolicitud.fromNombre(rs.getString("estado")));
        solicitud.setFechaSolicitud(rs.getTimestamp("fecha_solicitud").toLocalDateTime());

        int idAprobador = rs.getInt("id_aprobador");
        if (!rs.wasNull()) {
            new EmpleadoDAO().buscarPorId(idAprobador).ifPresent(solicitud::setAprobador);
        }
        java.sql.Timestamp fechaResolucion = rs.getTimestamp("fecha_resolucion");
        if (fechaResolucion != null) {
            solicitud.setFechaResolucion(fechaResolucion.toLocalDateTime());
        }
        solicitud.setComentario(rs.getString("comentario"));
        return solicitud;
    }
}
