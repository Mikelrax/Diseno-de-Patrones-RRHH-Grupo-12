package com.rrhh.dao;

import com.rrhh.interfaces.IDao;
import com.rrhh.modelo.Cargo;
import com.rrhh.modelo.Departamento;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.enums.EstadoEmpleado;
import com.rrhh.singleton.GestorConexionBD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpleadoDAO implements IDao<Empleado, Integer> {

    @Override
    public Empleado guardar(Empleado empleado) {
        String sql = "INSERT INTO empleados (nombre, apellido, dni, fecha_nacimiento, email, "
                + "fecha_contratacion, salario_base, estado, id_departamento, id_cargo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            enlazarParametros(stmt, empleado);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    empleado.setId(keys.getInt(1));
                }
            }
            return empleado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar empleado", e);
        }
    }

    @Override
    public Optional<Empleado> buscarPorId(Integer id) {
        String sql = "SELECT * FROM empleados WHERE id_empleado = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapearCompleto(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleado", e);
        }
    }

    public Optional<Empleado> buscarPorDni(String dni) {
        String sql = "SELECT * FROM empleados WHERE dni = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapearCompleto(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleado por DNI", e);
        }
    }

    @Override
    public List<Empleado> listarTodos() {
        String sql = "SELECT * FROM empleados ORDER BY apellido, nombre";
        List<Empleado> resultado = new ArrayList<>();
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapearCompleto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar empleados", e);
        }
        return resultado;
    }

    public List<Empleado> listarPorDepartamento(Integer idDepartamento) {
        String sql = "SELECT * FROM empleados WHERE id_departamento = ? ORDER BY apellido, nombre";
        List<Empleado> resultado = new ArrayList<>();
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idDepartamento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapearCompleto(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar empleados por departamento", e);
        }
        return resultado;
    }

    @Override
    public void actualizar(Empleado empleado) {
        String sql = "UPDATE empleados SET nombre = ?, apellido = ?, dni = ?, fecha_nacimiento = ?, "
                + "email = ?, fecha_contratacion = ?, salario_base = ?, estado = ?, id_departamento = ?, "
                + "id_cargo = ? WHERE id_empleado = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            enlazarParametros(stmt, empleado);
            stmt.setInt(11, empleado.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar empleado", e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM empleados WHERE id_empleado = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar empleado", e);
        }
    }

    Optional<Empleado> buscarBasicoPorId(Integer id) {
        String sql = "SELECT * FROM empleados WHERE id_empleado = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapearBasico(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleado básico", e);
        }
    }

    private void enlazarParametros(PreparedStatement stmt, Empleado empleado) throws SQLException {
        stmt.setString(1, empleado.getNombre());
        stmt.setString(2, empleado.getApellido());
        stmt.setString(3, empleado.getDni());
        stmt.setDate(4, empleado.getFechaNacimiento() != null ? Date.valueOf(empleado.getFechaNacimiento()) : null);
        stmt.setString(5, empleado.getEmail());
        stmt.setDate(6, Date.valueOf(empleado.getFechaContratacion()));
        stmt.setBigDecimal(7, empleado.getSalarioBase());
        stmt.setString(8, empleado.getEstado().name());
        if (empleado.getDepartamento() != null) {
            stmt.setInt(9, empleado.getDepartamento().getId());
        } else {
            stmt.setNull(9, Types.INTEGER);
        }
        if (empleado.getCargo() != null) {
            stmt.setInt(10, empleado.getCargo().getId());
        } else {
            stmt.setNull(10, Types.INTEGER);
        }
    }

    private Empleado mapearBasico(ResultSet rs) throws SQLException {
        Date fechaNacimiento = rs.getDate("fecha_nacimiento");
        return new Empleado(
                rs.getInt("id_empleado"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dni"),
                rs.getString("email"),
                fechaNacimiento != null ? fechaNacimiento.toLocalDate() : null,
                rs.getDate("fecha_contratacion").toLocalDate(),
                rs.getBigDecimal("salario_base"),
                EstadoEmpleado.valueOf(rs.getString("estado")),
                null,
                null
        );
    }

    private Empleado mapearCompleto(ResultSet rs) throws SQLException {
        Empleado empleado = mapearBasico(rs);

        int idDepartamento = rs.getInt("id_departamento");
        if (!rs.wasNull()) {
            Departamento departamento = new DepartamentoDAO().buscarPorId(idDepartamento).orElse(null);
            empleado.setDepartamento(departamento);
        }

        int idCargo = rs.getInt("id_cargo");
        if (!rs.wasNull()) {
            Cargo cargo = new CargoDAO().buscarPorId(idCargo).orElse(null);
            empleado.setCargo(cargo);
        }

        return empleado;
    }
}
