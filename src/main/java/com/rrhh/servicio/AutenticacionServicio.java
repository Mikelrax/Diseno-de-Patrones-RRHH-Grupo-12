package com.rrhh.servicio;

import com.rrhh.dao.UsuarioDAO;
import com.rrhh.modelo.Usuario;

import java.util.Optional;

public class AutenticacionServicio {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Optional<Usuario> autenticar(String username, String passwordPlano) {
        return usuarioDAO.buscarPorUsername(username)
                .filter(usuario -> usuario.validarCredenciales(passwordPlano));
    }
}
