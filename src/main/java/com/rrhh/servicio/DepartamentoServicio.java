package com.rrhh.servicio;

import com.rrhh.dao.DepartamentoDAO;
import com.rrhh.modelo.Departamento;
import com.rrhh.util.Validador;

import java.util.List;
import java.util.Optional;

public class DepartamentoServicio {

    private final DepartamentoDAO departamentoDAO = new DepartamentoDAO();

    public Departamento registrar(Departamento departamento) {
        Validador.requerido(departamento.getNombre(), "nombre");
        return departamentoDAO.guardar(departamento);
    }

    public void actualizar(Departamento departamento) {
        Validador.requerido(departamento.getNombre(), "nombre");
        departamentoDAO.actualizar(departamento);
    }

    public void eliminar(Integer id) {
        departamentoDAO.eliminar(id);
    }

    public Optional<Departamento> buscarPorId(Integer id) {
        return departamentoDAO.buscarPorId(id);
    }

    public List<Departamento> listarTodos() {
        return departamentoDAO.listarTodos();
    }
}
