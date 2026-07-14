package com.rrhh.interfaces;

import java.util.List;
import java.util.Optional;

public interface IDao<T, ID> {

    T guardar(T entidad);

    Optional<T> buscarPorId(ID id);

    List<T> listarTodos();

    void actualizar(T entidad);

    void eliminar(ID id);
}
