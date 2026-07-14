package com.rrhh.servicio;

import com.rrhh.dao.CargoDAO;
import com.rrhh.modelo.Cargo;

import java.util.List;

public class CargoServicio {

    private final CargoDAO cargoDAO = new CargoDAO();

    public Cargo registrar(Cargo cargo) {
        return cargoDAO.guardar(cargo);
    }

    public List<Cargo> listarTodos() {
        return cargoDAO.listarTodos();
    }
}
