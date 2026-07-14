package com.rrhh.dao;

import com.rrhh.factory.UsuarioFactory;
import com.rrhh.interfaces.IDao;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.Usuario;
import com.rrhh.modelo.enums.TipoUsuario;
import com.rrhh.singleton.GestorConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO implements IDao<Usuario, Integer> {

    @Override
    public Usuario guardar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password_hash, tipo_usuario, id_empleado, activo) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPasswordHash());
            stmt.setString(3, usuario.getTipoUsuario().name());
            stmt.setInt(4, usuario.getEmpleado().getId());
            stmt.setBoolean(5, usuario.isActivo());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    usuario.setId(keys.getInt(1));
                }
            }
            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar usuario", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario", e);
        }
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por username", e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuarios ORDER BY username";
        List<Usuario> resultado = new ArrayList<>();
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios", e);
        }
        return resultado;
    }

    @Override
    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET username = ?, password_hash = ?, tipo_usuario = ?, activo = ? "
                + "WHERE id_usuario = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPasswordHash());
            stmt.setString(3, usuario.getTipoUsuario().name());
            stmt.setBoolean(4, usuario.isActivo());
            stmt.setInt(5, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario", e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conexion = GestorConexionBD.obtenerInstancia().obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Empleado empleado = new EmpleadoDAO().buscarPorId(rs.getInt("id_empleado")).orElseThrow();
        TipoUsuario tipo = TipoUsuario.valueOf(rs.getString("tipo_usuario"));
        return UsuarioFactory.crear(
                tipo,
                rs.getInt("id_usuario"),
                rs.getString("username"),
                rs.getString("password_hash"),
                empleado,
                rs.getBoolean("activo")
        );
    }
}
